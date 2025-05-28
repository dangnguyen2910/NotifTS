package usth.intern.notifts.ui.manager.uistate

data class AppFilterUiState(
    val appList: List<String?> = listOf(),
    val appFilterDialogIsShown: Boolean = false,
    val appSelectionList: MutableList<String> = mutableListOf(),
    val onClickAppFilterButton: () -> Unit,
    val onDismissAppFilterDialog: () -> Unit,
    val updateAppFilterSelections: (String?) -> Unit,
    val onConfirmAppFilter: () -> Unit,
    val onCancelAppFilter: () -> Unit,
)
