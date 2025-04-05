package usth.intern.notifts

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.ui.text.intl.Locale
import org.w3c.dom.Text

private const val NTS_TAG = "NotificationToSpeechService"

class NotificationToSpeechService : NotificationListenerService(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech

    override fun onListenerConnected() {
        super.onListenerConnected()
        tts = TextToSpeech(this, this)
        Log.d(NTS_TAG, "Connected to notification to speech service")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d(NTS_TAG, "Disconnect to notification to speech service")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val packageName = sbn?.packageName
        val notification = sbn?.notification
        val extras = notification?.extras

        val title = extras?.getString("android.title") ?: "No title"
//        val text = extras?.getCharSequence("android.text") ?: "No text"
        val subText = extras?.getString("android.subText") ?: "No subtext"
        val bigText = extras?.getCharSequence("android.bigText")?.toString() ?: "No bigText"
        val textLines = extras?.getCharSequence("android.textLines")?.toString() ?: "No textLines"
        val messages = extras?.getString("android.messages") ?: "No messages"

//        sbn?.let {
//            Log.d("Extras", extras.toString())
//            Log.d("NotificationListener", "Package Name: $packageName")
//            Log.d("NotificationListener", "Title: $title")
//            Log.d("NotificationListener", "Text: $text")
//            Log.d("NotificationListener", "SubText: $subText")
//            Log.d("NotificationListener", "BigText: $bigText")
//            Log.d("NotificationListener", "TextLines: $textLines")
//            Log.d("NotificationListener", "Messages: $messages")
//        }

        // TODO: text to speech function or invoke function return notification
        tts.speak(bigText, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d(NTS_TAG, "Initializing text to speech: Success")

            tts.speak(
                "Hello",
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )
        }
//        val availableLanguages = tts.availableLanguages
//        val defaultLanguage = tts.defaultVoice.locale.country

//        for (locale in availableLanguages) {
//            if (locale.country == "VN") {
//                Log.d(NTS_TAG, locale.displayCountry)
//                Log.d(NTS_TAG, locale.displayLanguage)
//            }
//        }

//        Log.d(NTS_TAG, defaultLanguage)
    }

}