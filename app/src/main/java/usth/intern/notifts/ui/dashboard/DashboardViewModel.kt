package usth.intern.notifts.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usth.intern.notifts.data.repository.DatabaseRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-y")

    fun getNotificationCountByDay() {
        viewModelScope.launch(Dispatchers.IO) {
            val countList = databaseRepository.countNotificationLast7Days()
            val countDateMap = countList.associate { LocalDate.parse(it.notificationDate, formatter) to it.notificationCount }

            _uiState.update {
                it.copy(notificationCountByDate = countDateMap)
            }
        }
    }

    fun getNotificationCountByApp() {
        viewModelScope.launch(Dispatchers.IO) {
            val countList = databaseRepository.countNotificationByAppLast7Days()
            val countAppMap = countList.associate { it.app to it.notificationCount }

            _uiState.update {
                it.copy(notificationCountByApp = countAppMap)
            }
        }
    }
}