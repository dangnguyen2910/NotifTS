package usth.intern.notifts.ui.manager.uistate

data class KeywordsSearchBarUiState(
    val query: String = "",
    val onQueryChange: (String) -> Unit,
    val onSearch: (String) -> Unit,
)