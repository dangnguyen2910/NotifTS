package usth.intern.notifts.ui.manager

data class FilterUiState(
    val appFilterDialogShown: Boolean = false,
    val categoryFilterDialogShown: Boolean = false,
    val dateFilterDialogShown: Boolean = false,
    val appList: List<String> = listOf(),
    val categoryList: List<String?> = listOf(),
    val categorySelectionList: MutableList<String> = mutableListOf()
)
