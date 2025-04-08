package usth.intern.notifts.data

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log

private const val TAG = "NotificationToSpeechService"

class NotificationToSpeechService : NotificationListenerService(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private var serviceIsEnabled = true

    override fun onListenerConnected() {
        super.onListenerConnected()
        tts = TextToSpeech(this, this)
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
        val subText = extras?.getCharSequence("android.subText") ?: ""
        val bigText = extras?.getCharSequence("android.bigText")?: ""
        val textLines = extras?.getCharSequence("android.textLines")?: ""
        val messages = extras?.getCharSequence("android.messages") ?: ""

        Log.d(TAG, "-----------------------------------------------------")
        Log.d(TAG, packageName)
        Log.d(TAG, "Category: $category")
        Log.d(TAG, "Title: $title")
        Log.d(TAG, "Text: $text")
        Log.d(TAG, "SubText: $subText")
        Log.d(TAG, "Big Text: $bigText")
        Log.d(TAG, "Text Lines: $textLines")
        Log.d(TAG, "Messages: $messages")

        if (serviceIsEnabled){
            tts.speak("$title $text", TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d(TAG, "Initializing text to speech: Success")

            tts.speak(
                "Hello",
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )
        }
    }

}