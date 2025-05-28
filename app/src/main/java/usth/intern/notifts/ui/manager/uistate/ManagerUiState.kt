package usth.intern.notifts.ui.manager.uistate

import usth.intern.notifts.data.db.Notification

data class ManagerUiState (
    val notificationList: List<Notification> = listOf(),
    // Filter related
    val categoryList: List<String?> = listOf(),
    val categoryFilterDialogIsShown: Boolean = false,
    val categorySelectionList: MutableList<String?> = mutableListOf(),
    val isRefreshing: Boolean = false
)
