package usth.intern.notifts.domain

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import usth.intern.notifts.data.PreferenceRepository
import usth.intern.notifts.domain.tts.TtsEngine
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
    lateinit var settings: Settings

    private var isActivated: Boolean = false

    // This is to track the content of the newest notification
    // Initially used to know whether a notification is duplicated multiple times.
    private var previousText: String = ""

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        runBlocking {
            launch {
                /**
                 * If this value is false -> Notifications will not be spoken
                 */
                isActivated = preferenceRepository.isActivatedFlow.first()
            }
        }

        if (!isActivated) {
            return
        }

        super.onNotificationPosted(sbn)

        val category = sbn.notification.category
        val title = sbn.notification.extras.getCharSequence("android.title") ?: ""
        val text = sbn.notification.extras.getCharSequence("android.text") ?: ""
        val bigText = sbn.notification.extras.getCharSequence("android.bigText")?: ""
        val dateString = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            .format(Date(sbn.postTime))

        val notificationMap = mapOf(
            "packageName" to sbn.packageName,
            "category" to category.toString(),
            "title" to title.toString(),
            "text" to text.toString(),
            "bigText" to bigText.toString(),
            "dateString" to dateString
        )

        logNewNotification(notificationMap)

        runBlocking {
            launch {

                /**
                 * If this value is true then whenever screen is on, the notification is spoken.
                 * Else the notification is spoken only when screen is off.
                 */
                val speakerIsActivatedWhenScreenOn = preferenceRepository
                    .speakerIsEnabledWhenScreenOnFlow
                    .first()

                val isDuplicated = text == previousText

                val isAllowedToSpeak = isAllowedToSpeak(
                    isActivated = isActivated,
                    isDuplicated = isDuplicated,
                    speakerIsActivatedWhenScreenOn = speakerIsActivatedWhenScreenOn
                )
                if (isAllowedToSpeak) {
                    ttsEngine.run(notificationMap)
                }
                previousText = text.toString()
            }
        }

    }

    fun isAllowedToSpeak(
        isActivated: Boolean,
        isDuplicated: Boolean,
        speakerIsActivatedWhenScreenOn: Boolean
    ) : Boolean {

        if (!isActivated || isDuplicated) {
            return false
        }

        if (!settings.isScreenOn() || speakerIsActivatedWhenScreenOn) {
            return true
        }

        return false
    }

    private fun logNewNotification(notificationMap: Map<String, String>) {
        Log.d(TAG, "-----------------------------------------------------")
        Log.d(TAG, "Package name ${notificationMap["packageName"]} ")
        Log.d(TAG, "Category: ${notificationMap["category"]}")
        Log.d(TAG, "Title: ${notificationMap["title"]}")
        Log.d(TAG, "Text: ${notificationMap["text"]}")
        Log.d(TAG, "Big Text: ${notificationMap["bigText"]}")
        Log.d(TAG, "Date: ${notificationMap["dateString"]}")

    }
}