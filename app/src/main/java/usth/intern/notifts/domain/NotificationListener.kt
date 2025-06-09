package usth.intern.notifts.domain

import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import usth.intern.notifts.data.db.Notification
import usth.intern.notifts.data.repository.AppStatusRepository
import usth.intern.notifts.data.repository.DatabaseRepository
import usth.intern.notifts.data.repository.PreferenceRepository
import usth.intern.notifts.domain.tts.LanguageIdentifier
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
    lateinit var databaseRepository: DatabaseRepository
    @Inject lateinit var appStatusRepository: AppStatusRepository

    @Inject lateinit var languageIdentifier: LanguageIdentifier
    private lateinit var notificationFilter: NotificationFilter
    private lateinit var ignoredAppList: List<String>

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            ignoredAppList = appStatusRepository.getIgnoredApp()
            notificationFilter = NotificationFilter(
                ignoredAppList = ignoredAppList,
                preferenceRepository = preferenceRepository
            )
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        if (sbn == null)
            return

        // Retrieve some basic content of notification
        val category = sbn.notification.category ?: "Unknown"
        val title = sbn.notification.extras.getCharSequence("android.title") ?: ""
        val text = sbn.notification.extras.getCharSequence("android.text") ?: ""
        val bigText = sbn.notification.extras.getCharSequence("android.bigText")?: ""
        val timestamp = sbn.postTime
        val appName = getAppName(this, packageName = sbn.packageName) ?: "Unknown"

        val notification = Notification(
            packageName = appName,
            title = title.toString(),
            text = text.toString(),
            bigText = bigText.toString(),
            category = category,
            timestamp = timestamp
        )

        logNewNotification(notification)

        // Ignore duplicated notifications
        if (notificationFilter.filter(notification)) {
            Log.d(TAG, "This notification is blocked")
            return
        }
        else {
            CoroutineScope(Dispatchers.IO).launch {
                preferenceRepository.updatePreviousNotificationText(text.toString())
            }
        }

        // Detect language
        val language = languageIdentifier.predict(text.toString())
        Log.d("TtsEngine", "Language: $language")


        CoroutineScope(Dispatchers.IO).launch {
            databaseRepository.insertNotification(notification)
            Log.d(TAG, "Save notification complete")
        }

        runBlocking {
            launch {
                val isActivated = preferenceRepository.isActivatedFlow.first()
                Log.d("NotificationListener", "isActivated: $isActivated")

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
                    speakerIsActivatedWhenScreenOn = speakerIsActivatedWhenScreenOn
                )

                if (isAllowedToSpeak) {
                    ttsEngine.run(appName, title.toString(), text.toString(), language)
                }
            }
        }
    }

    fun isAllowedToSpeak(
        context: Context,
        isActivated: Boolean,
        speakerIsActivatedWhenScreenOn: Boolean
    ) : Boolean {

        if (!isActivated) {
            return false
        }

        if (!isScreenOn(context) || speakerIsActivatedWhenScreenOn) {
            return true
        }

        return false
    }

    private fun logNewNotification(notification: Notification) {
        Log.d(TAG, "-----------------------------------------------------")
        Log.d(TAG, "Package name: ${notification.packageName}")
        Log.d(TAG, "Category: ${notification.category}")
        Log.d(TAG, "Title: ${notification.title}")
        Log.d(TAG, "Text: ${notification.text}")
        Log.d(TAG, "Big Text: ${notification.bigText}")
        Log.d(TAG, "Date: ${SimpleDateFormat(
            "dd-MM-yyyy HH:mm",
            Locale.getDefault()).format(Date(notification.timestamp))}"
        )

    }

}