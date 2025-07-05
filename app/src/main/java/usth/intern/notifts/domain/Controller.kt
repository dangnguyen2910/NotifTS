package usth.intern.notifts.domain

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import usth.intern.notifts.data.repository.PreferenceRepository
import javax.inject.Inject

class Controller @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    @ApplicationContext private val context: Context,
) {
    suspend fun getModelDecision(language: String) : String {
        val isRemoteModelAllowed = preferenceRepository.isRemoteModelAllowed.first()
        val hasInternetConnection = hasInternetConnection(context)
        val isLanguageSupported = language == "ENGLISH" || language == "FRENCH"

        if (!isRemoteModelAllowed) {
            Log.e("Controller", "Remote model is not allowed by user.")
            return "local"
        }
        if (!hasInternetConnection) {
            Log.e("Controller", "There is no internet connection.")
            return "local"
        }
        if (!isLanguageSupported) {
            Log.e("Controller", "Language $language is not supported, yet.")
            return "local"
        }

        return "remote"
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