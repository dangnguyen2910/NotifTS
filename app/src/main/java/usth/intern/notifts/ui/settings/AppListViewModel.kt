package usth.intern.notifts.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usth.intern.notifts.data.repository.AppStatusRepository
import usth.intern.notifts.domain.AppStatus
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val appStatusRepository: AppStatusRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState = _uiState.asStateFlow()

    fun getAppStatusList() {
        viewModelScope.launch(Dispatchers.IO) {
            val appStatusList = appStatusRepository.getAllAppStatus().toMutableList()
            _uiState.update { it.copy(appStatusList = appStatusList) }
        }
    }

    fun updateSelection(appStatus: AppStatus) {
        viewModelScope.launch(Dispatchers.IO) {
            val newAppStatus = appStatus.copy(status = !appStatus.status)
            val index = _uiState.value.appStatusList.indexOfFirst { it.appName == appStatus.appName }
            if (index != -1) {
                _uiState.value.appStatusList[index] = newAppStatus
            }
            appStatusRepository.updateAppStatus(newAppStatus)
        }
    }

    fun getStatusList() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(statusList = appStatusRepository.getStatusList()) }
        }
    }
}