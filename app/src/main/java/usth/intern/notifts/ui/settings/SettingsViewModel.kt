package usth.intern.notifts.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import usth.intern.notifts.data.DatabaseRepository
import usth.intern.notifts.data.PreferenceRepository
import usth.intern.notifts.data.db.Notification
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel(){

    private var isActivated: Boolean = false
    private var speakerIsEnabledWhenScreenOn: Boolean = false
    private var speakerIsEnabledWhenDndOn: Boolean = false
    private var notificationIsShown: Boolean = false
    private var notification: Notification? = null

    // Use the old data in dataStore
    init {
        runBlocking {
            launch {
                notification = databaseRepository.newestNotification.first()
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
            }
        }
    }

    // Create the UI state
    private val homeUiState: SettingsUiState = SettingsUiState(
        isActivated = isActivated,
        speakerIsEnabledWhenScreenOn = speakerIsEnabledWhenScreenOn,
        speakerIsEnabledWhenDndOn = speakerIsEnabledWhenDndOn,
        notificationIsShown = notificationIsShown,
    )
    private val _uiState = MutableStateFlow(homeUiState)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    /**
     * Change the value of "Enable Speaker" switch and update to the data store.
     */
    fun onIsActivatedSwitchClicked() {
        _uiState.update { currentState ->
            currentState.copy(isActivated = !_uiState.value.isActivated)
        }

        runBlocking {
            launch {
                Log.d(TAG, "Calling updateActivateState in HomeViewModel")
                preferenceRepository.updateActivationState(_uiState.value.isActivated)
            }
        }
    }

    /**
     * Change the value on the "Screen On" switch and update the value to dataStore
     */
    fun onScreenOnSwitchClicked() {
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

}