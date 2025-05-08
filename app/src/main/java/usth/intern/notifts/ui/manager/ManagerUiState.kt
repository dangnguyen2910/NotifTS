package usth.intern.notifts.ui.manager

import usth.intern.notifts.data.db.Notification

data class ManagerUiState (
    val notificationList: List<Notification> = listOf(),
    val query: String = "",
    val isAppDialogShown: Boolean = false,
    // Filter related
    val appFilterDialogIsShown: Boolean = false,
    val categoryFilterDialogIsShown: Boolean = false,
    val dateFilterDialogIsShown: Boolean = false,
    val appList: List<String> = listOf(),
    val categoryList: List<String?> = listOf(),
    val categorySelectionList: MutableList<String?> = mutableListOf()
)
