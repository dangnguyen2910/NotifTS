package usth.intern.notifts.domain

import android.content.Context
import android.media.MediaPlayer
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import usth.intern.notifts.data.repository.DatabaseRepository
import usth.intern.notifts.data.repository.PreferenceRepository
import usth.intern.notifts.domain.tts.TtsEngine
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val TAG = "NotificationListener"

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    @Inject
    lateinit var ttsEngine: TtsEngine

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var databaseRepository: DatabaseRepository

    /**
     * If this value is false -> Notifications will not be spoken
     */
    private var isActivated: Boolean = false

    /**
     * To track the content of the newest notification.
     * Initially used to know whether a notification is duplicated multiple times.
     */
    private var previousText: String = ""

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) {
            return
        }
        super.onNotificationPosted(sbn)

        val category = sbn.notification.category ?: ""
        val title = sbn.notification.extras.getCharSequence("android.title") ?: ""
        val text = sbn.notification.extras.getCharSequence("android.text") ?: ""
        val bigText = sbn.notification.extras.getCharSequence("android.bigText")?: ""
        val date = sbn.postTime

        // Some apps push multiple same notifications for no reason, so we need to ignore them
        val isDuplicated = (text.toString() == previousText)
        if (isDuplicated) {
            return
        }

        previousText = text.toString()

        val appName = getAppName(this, packageName = sbn.packageName) ?: "Unknown"

        runBlocking {
            launch { isActivated = preferenceRepository.isActivatedFlow.first() }
            launch {
                databaseRepository.insertNotification(
                    packageName = appName,
                    title = title.toString(),
                    text = text.toString(),
                    bigText = bigText.toString(),
                    category = category,
                    date = date
                )
                Log.d(TAG, "Save notification complete")
            }
        }

        Log.d("NotificationListener", "isActivated: $isActivated")

        if (!isActivated) {
            return
        }

        logNewNotification(
            packageName = packageName,
            category = category,
            title = title.toString(),
            text = text.toString(),
            bigText = bigText.toString(),
            date = date
        )

        runBlocking {
            launch {
                /**
                 * If this value is true then whenever screen is on, the notification is spoken.
                 * Else the notification is spoken only when screen is off.
                 */
                val speakerIsActivatedWhenScreenOn = preferenceRepository
                    .speakerIsEnabledWhenScreenOnFlow
                    .first()

                val isAllowedToSpeak = isAllowedToSpeak(
                    context = this@NotificationListener,
                    isActivated = isActivated,
                    isDuplicated = isDuplicated,
                    speakerIsActivatedWhenScreenOn = speakerIsActivatedWhenScreenOn
                )

                if (isAllowedToSpeak) {
                    ttsEngine.run(title.toString(), text.toString())
                }
            }
        }
    }

    fun isAllowedToSpeak(
        context: Context,
        isActivated: Boolean,
        isDuplicated: Boolean,
        speakerIsActivatedWhenScreenOn: Boolean
    ) : Boolean {

        if (!isActivated || isDuplicated) {
            return false
        }

        if (!isScreenOn(context) || speakerIsActivatedWhenScreenOn) {
            return true
        }

        return false
    }

    private fun logNewNotification(
        packageName: String,
        category: String?,
        title: String,
        text: String?,
        bigText: String?,
        date: Long,
    ) {
        Log.d(TAG, "-----------------------------------------------------")
        Log.d(TAG, "Package name $packageName")
        Log.d(TAG, "Category: $category")
        Log.d(TAG, "Title: $title")
        Log.d(TAG, "Text: $text")
        Log.d(TAG, "Big Text: $bigText")
        Log.d(TAG, "Date: ${SimpleDateFormat(
            "dd-MM-yyyy HH:mm",
            Locale.getDefault()).format(Date(date))}"
        )

    }
}