package usth.intern.notifts.domain.tts

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import usth.intern.notifts.data.remote.WavApiService
import usth.intern.notifts.domain.NotificationContent
import usth.intern.notifts.domain.isWifiConnected
import java.io.File
import java.io.IOException
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

    fun run(app: String, title: String, text: String) {
        val notification = NotificationContent(app, title, text)
        if (isWifiConnected(context)) {
            try {
                playWav(context, notification)
            } catch (e: IOException) {
                useLocalTts(title, text)
            }
        } else {
            useLocalTts(title, text)
        }
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

    private fun useLocalTts(title: String, text: String) {
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

    private fun playWav(
        context: Context,
        notification: NotificationContent
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.51:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(WavApiService::class.java)
            val response = api.downloadWav(notification)

            if (response.isSuccessful && response.body() != null) {
                val tempFile = File.createTempFile("temp_audio", ".wav", context.cacheDir)
                val inputStream = response.body()!!.byteStream()
                val outputStream = tempFile.outputStream()

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                val mediaPlayer = MediaPlayer().apply {
                    setDataSource(tempFile.absolutePath)
                    prepare()
                    start()
                }

                mediaPlayer.setOnCompletionListener {
                    tempFile.delete()
                    mediaPlayer.release()
                }
            } else {
                Log.e("TtsEngine", "Failed to download audio: ${response.code()}")
            }
        }
    }
}

