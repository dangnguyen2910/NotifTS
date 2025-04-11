package usth.intern.notifts.data

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val TAG = "MyCustomEngine"

class Engine @Inject constructor(
    @ApplicationContext context: Context,
    private val preferenceRepository: PreferenceRepository,
) : TextToSpeech.OnInitListener{

    private val tts = TextToSpeech(context, this, "com.google.android.tts")

//    TODO: Use result from language detector to modify tts language
//    private val detector: LanguageDetector = LanguageDetectorBuilder.fromLanguages(
//        Language.ENGLISH,
//        Language.VIETNAMESE
//    ).build()

    override fun onInit(status: Int) {
        Log.d(TAG, "Initializing text to speech: Success")
        tts.speak(
            "Thanks for using my app",
            TextToSpeech.QUEUE_ADD,
            null,
            null
        )
    }

   fun run(prompt: String) {
//       val detectedLanguage: Language = detector.detectLanguageOf("prompt")
       runBlocking {
           launch {
               val isActivatedFlow: Flow<Boolean> =  preferenceRepository.isActivatedFlow

               Log.d(TAG, "willRead = ${isActivatedFlow.first()}")
               if (isActivatedFlow.first()) {
                   tts.speak(prompt, TextToSpeech.QUEUE_ADD, null, null)
               }
           }
       }
   }

}