package usth.intern.notifts.domain

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

    private val mutex = Mutex()

    override fun onCreate() {
        Log.i("NotificationListener", "Create notification listener service.")
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
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

        CoroutineScope(Dispatchers.IO).launch {
            mutex.withLock {
                val ignoredAppList = appStatusRepository.getIgnoredApp()

                val previousNotification = databaseRepository.loadNewestNotification()
                if (previousNotification != null) {
                    Log.d("NotificationListener",
                        "Previous notification: ${previousNotification.text}")
                }

                val notificationFilter = NotificationFilter(
                    ignoredAppList = ignoredAppList,
                    previousNotification = previousNotification
                )

                if (!notificationFilter.filter(notification)) {
                    Log.d("NotificationListener", "This notification will be spoken. ")
                    databaseRepository.insertNotification(notification)
                    Log.d(TAG, "Save notification complete")

                    ttsEngine.run(appName, title.toString(), text.toString())

                }
            }
        }
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