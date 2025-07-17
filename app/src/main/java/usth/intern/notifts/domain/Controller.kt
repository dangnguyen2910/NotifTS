package usth.intern.notifts.domain

import android.content.Context
import android.util.Log
import com.github.pemistahl.lingua.api.Language
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import usth.intern.notifts.data.repository.PreferenceRepository
import javax.inject.Inject

class Controller @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    @ApplicationContext private val context: Context,
) {
    suspend fun getModelDecision(language: Language) : ModelDecision {
        val isRemoteModelAllowed = preferenceRepository.isRemoteModelEnabled.first()
        val hasInternetConnection = hasInternetConnection(context)
        val isLanguageSupported = language == Language.ENGLISH || language == Language.FRENCH

        if (!isRemoteModelAllowed) {
            Log.e("Controller", "Remote model is not allowed by user.")
            return ModelDecision.LOCAL
        }
        if (!hasInternetConnection) {
            Log.e("Controller", "There is no internet connection.")
            return ModelDecision.LOCAL
        }
        if (!isLanguageSupported) {
            Log.e("Controller", "Language $language is not supported, yet.")
            return ModelDecision.LOCAL
        }

        return ModelDecision.REMOTE
    }

    suspend fun shouldSpeak() : Boolean {
        val isActivated = preferenceRepository.isActivatedFlow.first()

        val speakerIsActivatedWhenScreenOn = preferenceRepository
            .speakerIsEnabledWhenScreenOnFlow
            .first()

        val isScreenOn = isScreenOn(context)

        if (!isActivated) {
            Log.e("Controller", "Speaker is deactivated.")
            return false
        }

        if (!isScreenOn) {
            return true
        }

        if (speakerIsActivatedWhenScreenOn) {
            return true
        }

        Log.e("Controller", "The speaker is not allowed to speak while screen on.")
        return false
    }
}