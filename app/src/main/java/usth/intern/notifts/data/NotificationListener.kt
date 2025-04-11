package usth.intern.notifts.data

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "NotificationToSpeechService"

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    @Inject
    lateinit var engine: Engine

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Connected to notification to speech service")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d(TAG, "Disconnect to notification to speech service")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val packageName = sbn.packageName ?: ""
        val notification = sbn.notification
        val category = notification.category
        val extras = notification.extras

        val title = extras?.getCharSequence("android.title") ?: ""
        val text = extras?.getCharSequence("android.text") ?: ""
        val bigText = extras?.getCharSequence("android.bigText")?: ""

        Log.d(TAG, "-----------------------------------------------------")
        Log.d(TAG, packageName)
        Log.d(TAG, "Category: $category")
        Log.d(TAG, "Title: $title")
        Log.d(TAG, "Text: $text")
        Log.d(TAG, "Big Text: $bigText")

        val prompt = if (text == bigText) {
            "$title $text"
        } else {
            "$title $text $bigText"
        }

        engine.run(prompt)
    }
}