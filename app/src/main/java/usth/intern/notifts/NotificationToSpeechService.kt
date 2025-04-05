package usth.intern.notifts

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationToSpeechService : NotificationListenerService() {
    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NotificationToSpeechService", "Connected to notification to speech service")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val packageName = sbn?.packageName
        val extras = sbn?.notification?.extras

        val title = extras?.getString("android.title") ?: "No title"
//        val text = extras?.getCharSequence("android.text") ?: "No text"
        val subText = extras?.getString("android.subText") ?: "No subtext"
        val bigText = extras?.getCharSequence("android.bigText")?.toString() ?: "No bigText"
        val textLines = extras?.getCharSequence("android.textLines")?.toString() ?: "No textLines"
        val messages = extras?.getString("android.messages") ?: "No messages"

        sbn?.let {
//            Log.d("Extras", extras.toString())
            Log.d("NotificationListener", "Package Name: $packageName")
            Log.d("NotificationListener", "Title: $title")
//            Log.d("NotificationListener", "Text: $text")
            Log.d("NotificationListener", "SubText: $subText")
            Log.d("NotificationListener", "BigText: $bigText")
            Log.d("NotificationListener", "TextLines: $textLines")
            Log.d("NotificationListener", "Messages: $messages")
        }
    }
}