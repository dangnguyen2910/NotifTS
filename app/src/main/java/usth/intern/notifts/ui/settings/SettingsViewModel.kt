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

    private val _shouldPushNotification = MutableLiveData<Unit>()
    val shouldPushNotification: LiveData<Unit> = _shouldPushNotification

    private var isActivated: Boolean = false
    private var speakerIsEnabledWhenScreenOn: Boolean = false
    private var speakerIsEnabledWhenDndOn: Boolean = false
    private var notificationIsShown: Boolean = false
    private var isRemoteModelEnabled = false
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
                isRemoteModelEnabled = preferenceRepository.isRemoteModelEnabled.first()
            }
        }
    }

    // Create the UI state
    private val settingsUiState: SettingsUiState = SettingsUiState(
        isActivated = isActivated,
        speakerIsEnabledWhenScreenOn = speakerIsEnabledWhenScreenOn,
        speakerIsEnabledWhenDndOn = speakerIsEnabledWhenDndOn,
        isRemoteModelEnabled = isRemoteModelEnabled,
        notificationIsShown = notificationIsShown,
        currentEnglishVoice = currentEnglishVoice,
        currentFrenchVoice = currentFrenchVoice,
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

        viewModelScope.launch {
            preferenceRepository.updateScreenOnActivationState(
                _uiState.value.speakerIsEnabledWhenScreenOn
            )
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

    fun onClickTest() {
        _uiState.update { it.copy(isTestDialogShown = true) }
    }

    fun onDismissTestDialog() {
        _uiState.update { it.copy(isTestDialogShown = false) }
    }

    fun onConfirmTest(content: Pair<String, String>) {
        _uiState.update { it.copy(testNotificationTitle = content.first, testNotificationText = content.second) }
        _shouldPushNotification.value = Unit
    }

    fun onEnableRemoteModelSwitchToggled() {
        Log.d("SettingsViewModel", "Toggle the Enable Remote Model Switch.")
        _uiState.update { it.copy(isRemoteModelEnabled = !_uiState.value.isRemoteModelEnabled)}
        viewModelScope.launch {
            preferenceRepository.setRemoteModelEnabled(_uiState.value.isRemoteModelEnabled)
        }
    }
}