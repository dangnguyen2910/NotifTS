package usth.intern.notifts.data

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val TAG = "NotificationToSpeechService"

@AndroidEntryPoint
class NotificationToSpeechService : NotificationListenerService(), TextToSpeech.OnInitListener {
    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    private lateinit var tts: TextToSpeech
    private lateinit var engine: Engine


    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Connected to notification to speech service")
        tts = TextToSpeech(this, this)
        engine = Engine(tts, preferenceRepository)
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

        if (!tts.isSpeaking){
            engine.run("$title $text")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d(TAG, "Initializing text to speech: Success")

            runBlocking {
                launch {
                    engine.run("Hello")
                }
            }
        }
    }

}