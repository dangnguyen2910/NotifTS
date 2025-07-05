package usth.intern.notifts.domain.tts

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import usth.intern.notifts.data.remote.WavApiService
import usth.intern.notifts.data.repository.PreferenceRepository
import usth.intern.notifts.domain.Controller
import usth.intern.notifts.domain.NotificationPackage
import usth.intern.notifts.domain.hasInternetConnection
import java.io.File
import java.io.IOException
import javax.inject.Inject

class TtsEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferenceRepository: PreferenceRepository,
    private val languageIdentifier: LanguageIdentifier,
    private val controller: Controller
) {
    private lateinit var tts: TextToSpeech

    suspend fun run(app: String, title: String, text: String) {
        // Detect language
        val language = languageIdentifier.predict(text)
        Log.d("TtsEngine", "Language: $language")

        val modelDecision = controller.getModelDecision(language)
        val shouldSpeak = controller.shouldSpeak()

        // Terminate if the controller doesn't allow to speak.
        if (!shouldSpeak)
            return

        // Create notification object.
        val englishVoice = preferenceRepository.englishVoice.first()
        val frenchVoice = preferenceRepository.frenchVoice.first()

        val notification = when (language) {
            "ENGLISH" -> NotificationPackage(app, title, text, language, englishVoice)
            "FRENCH" -> NotificationPackage(app, title, text, language, frenchVoice)
            else -> NotificationPackage(app, title, text, language, "")
        }

        // Use local model if the controller says so.
        if (modelDecision == "local") {
            useLocalTts(notification)
            return
        }

        // Use remote model, roll back to local if any error happens.
        try {
            Log.d("TtsEngine", "Use remote model")
            useRemoteTts(context, notification)
        } catch (e: IOException) {
            Log.e("TtsEngine", e.toString())
            useLocalTts(notification)
        }
    }

    private fun useLocalTts(notification: NotificationPackage) {
        tts = TextToSpeech(context, { status ->
            if (status == TextToSpeech.SUCCESS) {
                Log.d("TtsEngine", "Initialize TTS engine success")

                val finalText = "${notification.app}. ${notification.title}. ${notification.text}"

                tts.speak(finalText, TextToSpeech.QUEUE_ADD, null, null)

            } else {
                Log.e("TtsEngine", "Initialize TTS engine fail")
            }
        }, "com.google.android.tts")
    }

    private fun useRemoteTts(
        context: Context,
        notification: NotificationPackage
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.176.229:5000/")
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
                    useLocalTts(notification)
                }
            } catch (e: Exception) {
                Log.e("TtsEngine", e.toString())
                useLocalTts(notification)
            }
        }
    }
}

