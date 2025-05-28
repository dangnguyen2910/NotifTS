package usth.intern.notifts.ui.manager.uistate

data class CategoryFilterUiState(
    val categoryList: List<String?> = listOf(),
    val categoryFilterDialogIsShown: Boolean = false,
    val categorySelectionList: MutableList<String?> = mutableListOf(),
    val onClickCategoryFilterButton: () -> Unit,
    val onDismissCategoryFilterDialog: () -> Unit,
    val updateCategoryFilterSelections: (String?) -> Unit,
    val onConfirmCategoryFilter: () -> Unit,
    val onCancelCategoryFilter: () -> Unit,

)