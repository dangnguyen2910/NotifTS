package usth.intern.notifts.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import usth.intern.notifts.data.repository.PreferenceRepository
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel(){

    private val _openTtsSetting = MutableLiveData<Unit>()
    val openTtsSetting: LiveData<Unit> = _openTtsSetting

    private var isActivated: Boolean = false
    private var speakerIsEnabledWhenScreenOn: Boolean = false
    private var speakerIsEnabledWhenDndOn: Boolean = false
    private var notificationIsShown: Boolean = false
    private lateinit var currentEnglishVoice: String
    private lateinit var currentFrenchVoice: String

    // Use the old data in dataStore
    init {
        runBlocking {
            launch {
                isActivated = preferenceRepository.isActivatedFlow.first()
                speakerIsEnabledWhenScreenOn = preferenceRepository
                    .speakerIsEnabledWhenScreenOnFlow
                    .first()
                speakerIsEnabledWhenDndOn = preferenceRepository
                    .speakerIsEnabledWhenDndOnFlow
                    .first()
                notificationIsShown = preferenceRepository
                    .notificationIsShownFlow
                    .first()
                currentEnglishVoice = preferenceRepository.englishVoice.first()
                currentFrenchVoice = preferenceRepository.frenchVoice.first()
            }
        }
    }

    // Create the UI state
    private val settingsUiState: SettingsUiState = SettingsUiState(
        isActivated = isActivated,
        speakerIsEnabledWhenScreenOn = speakerIsEnabledWhenScreenOn,
        speakerIsEnabledWhenDndOn = speakerIsEnabledWhenDndOn,
        notificationIsShown = notificationIsShown,
        currentEnglishVoice = currentEnglishVoice,
        currentFrenchVoice = currentFrenchVoice
    )
    private val _uiState = MutableStateFlow(settingsUiState)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    /**
     * Change the value of "Enable Speaker" switch and update to the data store.
     */
    fun enableSpeaker() {
        _uiState.update { currentState ->
            currentState.copy(isActivated = !_uiState.value.isActivated)
        }

        viewModelScope.launch {
            launch {
                Log.d(TAG, "Calling enableSpeaker in HomeViewModel")
                preferenceRepository.updateActivationState(_uiState.value.isActivated)
            }
        }
    }

    /**
     * Change the value on the "Screen On" switch and update the value to dataStore
     */
    fun enableSpeakerWhenScreenOn() {
        _uiState.update { currentState ->
            currentState.copy(
                speakerIsEnabledWhenScreenOn = !_uiState.value.speakerIsEnabledWhenScreenOn
            )
        }

        runBlocking {
            launch {
                preferenceRepository.updateScreenOnActivationState(
                    _uiState.value.speakerIsEnabledWhenScreenOn
                )
            }
        }
    }

    /**
     * Change the value on the "DND on" switch and update the value to dataStore
     */
    @Deprecated("No longer used")
    fun onDndOnSwitchClicked() {
        _uiState.update { currentState ->
            currentState.copy(
                speakerIsEnabledWhenDndOn = !_uiState.value.speakerIsEnabledWhenDndOn
            )
        }

        runBlocking {
            launch {
                preferenceRepository.updateDndOnActivationState(
                    _uiState.value.speakerIsEnabledWhenDndOn
                )
            }
        }
    }

    /**
     * Change the value on the "Display notification" switch and update the value to dataStore
     */
    @Deprecated("No longer used")
    fun onNotificationOnSwitchClicked() {
        _uiState.update { currentState ->
            currentState.copy(
                notificationIsShown = !_uiState.value.notificationIsShown
            )
        }

        runBlocking {
            launch {
                preferenceRepository.updateNotificationOnActivationState(
                    _uiState.value.notificationIsShown
                )
            }
        }
    }

    fun chooseLocalTtsModel() {
        Log.d("settingsVM", "ChooseLocalModel is triggered")
        _openTtsSetting.value = Unit
    }

    fun onClickVoiceSelection() {
        Log.d("settingsVM", "Click voice selection")
        _uiState.update {
            it.copy(showVoiceDialog = true)
        }
    }

    fun onDismissVoiceSelection() {
        _uiState.update {
            it.copy(showVoiceDialog = false)
        }
    }

    fun onEnglishVoiceSelected(voice: String) {
        Log.d("settingsVM", "Select english voice: $voice")
        _uiState.update { it.copy(currentEnglishVoice = voice) }
         viewModelScope.launch {
             preferenceRepository.updateEnglishVoice(voice)
         }
    }
}