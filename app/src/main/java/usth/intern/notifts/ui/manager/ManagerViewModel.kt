package usth.intern.notifts.ui.manager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usth.intern.notifts.data.DatabaseRepository
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ManagerViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
)  : ViewModel(){

    private val _uiState = MutableStateFlow(ManagerUiState())
    val uiState = _uiState.asStateFlow()

    fun onReload() {
        _uiState.update { currentState ->
            currentState.copy(isRefreshing = true)
        }
        Log.d("ManagerViewModel", "isRefreshing: ${_uiState.value.isRefreshing}")
        viewModelScope.launch {
            delay(200)
            _uiState.update { currentState ->
                currentState.copy(
                    notificationList = databaseRepository.loadAllNotification().first()
                )
            }
            _uiState.update {
                it.copy(isRefreshing = false)
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
                    notificationList = databaseRepository.loadNotificationByKeywords(query)
                )
            }
        }
    }

    fun onClickAppFilterButton() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentState ->
                currentState.copy(
                    appFilterDialogIsShown = true,
                    appList = databaseRepository.loadUniquePackages()
                )
            }
        }
    }

    fun onDismissAppFilterDialog() {
        _uiState.update { currentState ->
            currentState.copy(appFilterDialogIsShown = false)
        }
    }

    fun onClickCategoryFilterButton() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentState ->
                currentState.copy(
                    categoryFilterDialogIsShown = true,
                    categoryList = databaseRepository.loadUniqueCategories()
                )
            }
        }
    }

    fun onDismissCategoryFilterDialog() {
        _uiState.update { currentState ->
            currentState.copy(categoryFilterDialogIsShown = false)
        }
    }

    fun onClickDateFilterButton() {
        _uiState.update { currentState ->
            currentState.copy(dateFilterDialogIsShown = true)
        }
    }

    fun onDismissDateFilterDialog() {
        _uiState.update { currentState ->
            currentState.copy(dateFilterDialogIsShown = false)
        }
    }

    /**
     * This function is called when a checkbox is checked. It append the input category to the
     * ui state.
     * @param category: The name of category whose checkbox is checked
     */
    fun updateCategoryFilterSelections(category: String?) {
        // Dealing with null category
        if (category == null || category == "") {
            val newCategory = "Unknown"
            if (newCategory !in _uiState.value.categorySelectionList) {
                _uiState.value.categorySelectionList.add(newCategory)
            } else {
                _uiState.value.categorySelectionList.remove(newCategory)
            }
            return
        }

        if (category !in _uiState.value.categorySelectionList) {
            _uiState.value.categorySelectionList.add(category)
        } else {
            _uiState.value.categorySelectionList.remove(category)
        }
    }

    /**
     * This function is called when user click the Cancel button
     * in Category filter dialog. It will clear the category selection list
     * in ui state.
     */
    fun onCancelCategoryFilter() {
        _uiState.value.categorySelectionList.clear()
        Log.d("FilterViewModel", "${_uiState.value.categorySelectionList.isEmpty()}")
    }

    /**
     * This function is called when user click the Confirm button
     * in Category filter dialog. It will update the notification
     * list in manager screen that correspond to the categories user
     * chooses.
     */
    fun onConfirmCategoryFilter() {
        // Do nothing if no option is selected
        if (_uiState.value.categorySelectionList.isEmpty()) {
            return
        }

        val containNull = _uiState.value.categorySelectionList.contains(null)

        viewModelScope.launch(Dispatchers.IO) {
            val notificationList = databaseRepository.loadNotificationByCategories(
                categorySelectionList = _uiState.value.categorySelectionList.toList(),
                containNull = containNull
            ).first()
            _uiState.update { currentState ->
                currentState.copy(notificationList = notificationList)
            }
            onCancelCategoryFilter()

            Log.d("ManagerViewModel", "onConfirmCategoryFilter is done")
        }
    }

    /**
     * Similar to [updateCategoryFilterSelections] but for app instead
     */
    fun updateAppFilterSelections(app: String) {
        if (app !in _uiState.value.categorySelectionList) {
            _uiState.value.appSelectionList.add(app)
        } else {
            _uiState.value.appSelectionList.remove(app)
        }
        Log.d("ManagerViewModel", "updateAppFilterSelections is called")
    }

    /**
     * Similar to [onConfirmCategoryFilter] but for app instead.
     */
    fun onConfirmAppFilter() {
        if (_uiState.value.appSelectionList.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val notificationList = databaseRepository.loadNotificationByApps(
                appSelectionList = _uiState.value.appSelectionList.toList()
            ).first()
            _uiState.update { currentState ->
                currentState.copy(notificationList = notificationList)
            }

            // Delete app selection list
            onCancelAppFilter()
        }
        Log.d("ManagerViewModel", "onConfirmAppFilter is done")

    }

    /**
     * Similar to [onCancelCategoryFilter] but for app instead.
     */
    fun onCancelAppFilter() {
        _uiState.value.appSelectionList.clear()
    }

    /**
     * This function is triggered when Confirm button of Date Filter Dialog is clicked.
     * @param datePair: Pair of date chosen by user.
     */
    fun onDateRangeSelected(datePair: Pair<Long?, Long?>) {
        // If first date is null -> Do nothing
        if (datePair.first == null) {
            return
        }

        val offset = calculateTimeOffsetFromUTC()

        // If second date is null -> filter only the first date
        // else date range.
        viewModelScope.launch {
            val firstDate = datePair.first!! - offset

            if (datePair.second == null) {
                val notificationList = databaseRepository
                    .loadNotificationByDate(firstDate)
                    .first()

                _uiState.update { currentState ->
                    currentState.copy(notificationList = notificationList)
                }
            } else {
                val secondDate = datePair.second!! - offset
                val notificationList = databaseRepository.loadNotificationByDateRange(
                    firstDate,
                    secondDate
                ).first()

                _uiState.update { currentState ->
                    currentState.copy(notificationList = notificationList)
                }
            }
        }
    }

    private fun calculateTimeOffsetFromUTC() : Long {
        val now = ZonedDateTime.now()
        val offset = now.offset
        return offset.totalSeconds * 1000L
    }
}