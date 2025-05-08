package usth.intern.notifts.ui.manager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
     * in Category filter dialog.
     */
    fun onConfirmCategoryFilter() {
        // TODO: implement me 
        // Do nothing if no option is selected
        if (_uiState.value.categorySelectionList.isEmpty()) {
            return
        }
    }
}