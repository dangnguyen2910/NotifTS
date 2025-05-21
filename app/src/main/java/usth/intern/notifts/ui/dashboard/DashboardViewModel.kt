package usth.intern.notifts.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usth.intern.notifts.data.DatabaseRepository
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    fun getNotificationCountByDay() {
        viewModelScope.launch(Dispatchers.IO) {
            val countList = databaseRepository.countNotificationLast7Days()
            val countDateMap = countList.associate { it.notificationDate to it.notificationCount }

            _uiState.update {
                it.copy(notificationCountByDate = countDateMap)
            }
        }
    }
}