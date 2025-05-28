package usth.intern.notifts.ui.manager.uistate

import usth.intern.notifts.data.db.Notification

data class ManagerUiState (
    val notificationList: List<Notification> = listOf(),
    val isRefreshing: Boolean = false
)
