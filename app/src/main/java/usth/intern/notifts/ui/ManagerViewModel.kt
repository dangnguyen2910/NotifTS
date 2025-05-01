package usth.intern.notifts.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import usth.intern.notifts.data.DatabaseRepository
import javax.inject.Inject

class ManagerViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
)  : ViewModel(){
    private val _uiState = MutableStateFlow(ManagerUiState())
    val uiState = _uiState.asStateFlow()

    //todo
//    fun onReload() {
//        _uiState.update { managerUiState ->
//            managerUiState
//        }
//    }
}