package usth.intern.notifts.data

import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "MyCustomEngine"

class Engine(
    private val model: TextToSpeech,
    private val preferenceRepository: PreferenceRepository
) {

   fun run(prompt: String) {
       runBlocking {
           launch {
               val isActivatedFlow: Flow<Boolean> =  preferenceRepository.isActivatedFlow

               Log.d(TAG, "willRead = ${isActivatedFlow.first()}")
               if (isActivatedFlow.first()) {
                   model.speak(prompt, TextToSpeech.QUEUE_FLUSH, null, null)
               }
           }
       }
   }

}