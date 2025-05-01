package usth.intern.notifts.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import usth.intern.notifts.data.DatabaseRepository
import javax.inject.Inject

@HiltViewModel
class ManagerViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
)  : ViewModel(){

    private val _uiState = MutableStateFlow(ManagerUiState())
    val uiState = _uiState.asStateFlow()

    //todo
    fun onReload() {
        runBlocking {
            launch {
                _uiState.update { currentState ->
                    currentState.copy(
                        notificationList = databaseRepository.loadAllNotification().first()
                    )
                }
                Log.d("ManagerViewModel", "On reload is called")
            }
        }
    }
}