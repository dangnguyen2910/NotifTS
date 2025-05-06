package usth.intern.notifts.ui.manager

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
class FilterViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState = _uiState.asStateFlow()

    fun onClickAppFilterButton() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentState ->
                currentState.copy(
                    appFilterDialogShown = true,
                    appList = databaseRepository.loadUniquePackages()
                )
            }
        }
    }

    fun onDismissAppFilterDialog() {
        _uiState.update { currentState ->
            currentState.copy(appFilterDialogShown = false)
        }
    }

    fun onClickCategoryFilterButton() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentState ->
                currentState.copy(
                    categoryFilterDialogShown = true,
                    categoryList = databaseRepository.loadUniqueCategories()
                )
            }
        }
    }

    fun onDismissCategoryFilterDialog() {
        _uiState.update { currentState ->
            currentState.copy(categoryFilterDialogShown = false)
        }
    }

    fun onClickDateFilterButton() {
        _uiState.update { currentState ->
            currentState.copy(dateFilterDialogShown = true)
        }
    }

    fun onDismissDateFilterDialog() {
        _uiState.update { currentState ->
            currentState.copy(dateFilterDialogShown = false)
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

}
