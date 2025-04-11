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
import usth.intern.notifts.data.PreferenceRepository
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel(){

    private var isActivated: Boolean = false

    init {
        runBlocking {
            launch {
                isActivated = preferenceRepository.isActivatedFlow.first()
                preferenceRepository.updateActivationState(isActivated)
            }
        }
    }

    private val _uiState = MutableStateFlow(HomeUiState(isActivated))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


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