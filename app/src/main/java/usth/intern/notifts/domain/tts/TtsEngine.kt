package usth.intern.notifts.domain.tts

import android.content.Context
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

private const val TAG = "MyCustomEngine"

class TtsEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val languageIdentifier: LanguageIdentifier,
) {
    private lateinit var tts: TextToSpeech

    private val localeMap: MutableMap<String, Locale?> = mutableMapOf(
        "ENGLISH" to null,
        "VIETNAMESE" to null,
    )

    fun run(notificationMap: Map<String, String>) {
        val title = notificationMap["title"] ?: ""
        val text = notificationMap["text"] ?: ""

        tts = TextToSpeech(context, { status ->
            if (status == TextToSpeech.SUCCESS) {
                Log.d(TAG, "Initialize TTS engine success")

                val availableLocale = tts.availableLanguages
                if (availableLocale != null) {
                    for (locale in availableLocale) {
                        when (locale.displayLanguage) {
                            "Vietnamese" -> localeMap["VIETNAMESE"] = locale
                            "English" -> localeMap["ENGLISH"] = locale
                        }
                    }
                }

                speak(title, languageIdentifier.predict(title))
                speak(text, languageIdentifier.predict(text))

            } else {
                Log.e(TAG, "Initialize TTS engine fail")
            }
        }, "com.google.android.tts")

    }

    private fun speak(text: String, language: String) : Int {
        Log.d(TAG, "Language: $language")
        if (language == "UNKNOWN") {
            return TextToSpeech.ERROR
        }

        val result = tts.setLanguage(localeMap[language])

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            Log.e(TAG, "Language is not supported or missing data")

        val speakResult = tts.speak(text, TextToSpeech.QUEUE_ADD, null, null)
        return speakResult
    }
}

//TODO: Move this to correct place

//                Insert notification to database
//               databaseRepository.insertNotification(
//                   packageName = packageName,
//                   title = title.toString(),
//                   text = text.toString(),
//                   bigText = bigText.toString(),
//                   category = category.toString(),
//                   date = dateString
//               )
