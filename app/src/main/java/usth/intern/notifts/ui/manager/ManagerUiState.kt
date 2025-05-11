package usth.intern.notifts.ui.manager

import usth.intern.notifts.data.db.Notification

data class ManagerUiState (
    val notificationList: List<Notification> = listOf(),
    val query: String = "",
    val isAppDialogShown: Boolean = false,
    // Filter related
    val appList: List<String> = listOf(),
    val appFilterDialogIsShown: Boolean = false,
    val appSelectionList: MutableList<String> = mutableListOf(),
    val categoryList: List<String?> = listOf(),
    val categoryFilterDialogIsShown: Boolean = false,
    val categorySelectionList: MutableList<String?> = mutableListOf(),
    val dateFilterDialogIsShown: Boolean = false,
    val isRefreshing: Boolean = false
)
