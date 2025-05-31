package usth.intern.notifts.domain.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class TtsEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val languageIdentifier: LanguageIdentifier,
) {
    private lateinit var tts: TextToSpeech

    private val localeMap: MutableMap<String, Locale?> = mutableMapOf(
        "ENGLISH" to null,
        "VIETNAMESE" to null,
        "FRENCH" to null,
    )

    fun run(title: String, text: String) {
        tts = TextToSpeech(context, { status ->
            if (status == TextToSpeech.SUCCESS) {
                Log.d("TtsEngine", "Initialize TTS engine success")

                val availableLocale = tts.availableLanguages
                if (availableLocale != null) {
                    for (locale in availableLocale) {
                        when (locale.displayLanguage) {
                            "Vietnamese" -> localeMap["VIETNAMESE"] = locale
                            "English" -> localeMap["ENGLISH"] = locale
                        }
                    }
                }

                val language = languageIdentifier.predict(text)

                speak(title, language)
                speak(text, language)

            } else {
                Log.e("TtsEngine", "Initialize TTS engine fail")
            }
        }, "com.google.android.tts")

    }

    private fun speak(text: String, language: String) : Int {
        Log.d("TtsEngine", "Language: $language")
        if (language == "UNKNOWN") {
            return TextToSpeech.ERROR
        }

        val result = tts.setLanguage(localeMap[language])

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TtsEngine", "Language is not supported or missing data")
            return TextToSpeech.ERROR
        }

        val speakResult = tts.speak(text, TextToSpeech.QUEUE_ADD, null, null)
        return speakResult

    }
}

