package usth.intern.notifts.ui.manager.uistate

data class DateFilterUiState(
    val onClickDateFilterButton: () -> Unit,
    val dateFilterDialogIsShown: Boolean = false,
    val onDismissDateFilterDialog: () -> Unit,
    val onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
)
