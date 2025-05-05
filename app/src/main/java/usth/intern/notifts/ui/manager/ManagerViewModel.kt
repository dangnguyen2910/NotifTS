package usth.intern.notifts.ui.manager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usth.intern.notifts.data.DatabaseRepository
import javax.inject.Inject

@HiltViewModel
class ManagerViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
)  : ViewModel(){

    private val _uiState = MutableStateFlow(ManagerUiState())
    val uiState = _uiState.asStateFlow()

    fun onReload() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    notificationList = databaseRepository.loadAllNotification().first()
                )
            }
            Log.d("ManagerViewModel", "On reload is called")
        }
    }

    fun onTypingSearch(query: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    query = query
                )
            }
        }
    }

    fun onEnterSearch(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {currentState ->
                currentState.copy(
                    notificationList = databaseRepository.loadNotificationWithKeywords(query)
                )
            }
        }
    }
}