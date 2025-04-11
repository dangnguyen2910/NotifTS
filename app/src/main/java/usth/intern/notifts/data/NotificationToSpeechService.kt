package usth.intern.notifts.data

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetector
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject

private const val TAG = "NotificationToSpeechService"

@AndroidEntryPoint
class NotificationToSpeechService : NotificationListenerService(), TextToSpeech.OnInitListener {
    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    private lateinit var tts: TextToSpeech
    private lateinit var engine: Engine
    private val detector: LanguageDetector = LanguageDetectorBuilder.fromLanguages(
        Language.ENGLISH,
        Language.VIETNAMESE
    ).build()


    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Connected to notification to speech service")

        // Initialize tts model and engine
        // TODO: Check if there is a way to separate tts to engine file.
        tts = TextToSpeech(this, this, "com.google.android.tts")
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

        val prompt = if (text == bigText) {
            "$title $text"
        } else {
            "$title $text $bigText"
        }

        val detectedLanguage = detector.detectLanguageOf(title.toString())
        Log.d(TAG, detectedLanguage.name)
        Log.d(TAG, "isSpeaking = ${tts.isSpeaking}")

        if (!tts.isSpeaking){
            engine.run(prompt)
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