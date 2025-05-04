package usth.intern.notifts.ui.manager

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel() {
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
        _uiState.update { currentState ->
            currentState.copy(categoryFilterDialogShown = true)
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
