package usth.intern.notifts.data

import android.content.Context
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.LANG_MISSING_DATA
import android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED
import android.util.Log
import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetector
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject

private const val TAG = "MyCustomEngine"

class Engine @Inject constructor(
    @ApplicationContext context: Context,
    private val preferenceRepository: PreferenceRepository,
) : TextToSpeech.OnInitListener{

    private val tts = TextToSpeech(context, this, "com.google.android.tts")

    private val detector: LanguageDetector = LanguageDetectorBuilder.fromLanguages(
        Language.ENGLISH,
        Language.VIETNAMESE,
        Language.FRENCH,
    ).build()

    private val localeMap: MutableMap<String, Locale?> = mutableMapOf(
        "ENGLISH" to null,
        "VIETNAMESE" to null,
    )

    override fun onInit(status: Int) {
        Log.d(TAG, "Initializing text to speech: Success")
        tts.speak(
            "Thanks for using my app",
            TextToSpeech.QUEUE_ADD,
            null,
            null
        )

        val availableLocale = tts.availableLanguages
        for (locale in availableLocale) {
            when (locale.displayLanguage) {
                "Vietnamese" -> localeMap["VIETNAMESE"] = locale
                "English" -> localeMap["ENGLISH"] = locale
            }

        }

    }

   fun run(sbn: StatusBarNotification) {
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

       runBlocking {
           launch {
               val isActivatedFlow: Flow<Boolean> =  preferenceRepository.isActivatedFlow

               if (isActivatedFlow.first()) {
                   engineSpeak(title.toString())
                   engineSpeak(text.toString())

                   if (bigText != text) {
                       engineSpeak(bigText.toString())
                  }
               }
           }
       }
   }

   private fun engineSpeak(text: String) {
       val detectedLanguageOfText: Language = detector.detectLanguageOf(text)
       val result = tts.setLanguage(localeMap[detectedLanguageOfText.name])

       if (result == LANG_MISSING_DATA || result == LANG_NOT_SUPPORTED)
           Log.e("TTS", "Language is not supported or missing data")

       tts.speak(text, TextToSpeech.QUEUE_ADD, null, null)

   }
}