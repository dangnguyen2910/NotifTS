package usth.intern.notifts.ui

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
    private var notification: Notification? = null

    // Use the old data in dataStore
    init {
        runBlocking {
            launch {
                notification = databaseRepository.newestNotification.first()
                isActivated = preferenceRepository.isActivatedFlow.first()
                preferenceRepository.updateActivationState(isActivated)
            }
        }
    }

    // Create the UI state
    private val homeUiState: SettingsUiState = SettingsUiState(
        isActivated = isActivated,
    )
    private val _uiState = MutableStateFlow(homeUiState)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    /**
     * Change the value of switch and update to the data store.
     */
    fun onSwitchClicked() {
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
}