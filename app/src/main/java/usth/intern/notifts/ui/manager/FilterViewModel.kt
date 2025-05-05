package usth.intern.notifts.ui.manager

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
        _uiState.update { currentState ->
            currentState.copy(appFilterDialogShown = true)
        }
    }

    fun onDismissAppFilterDialog() {
        _uiState.update { currentState ->
            currentState.copy(appFilterDialogShown = false)
        }
    }

    fun onClickCategoryFilterButton() {
        CoroutineScope(Dispatchers.IO).launch {
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

}
