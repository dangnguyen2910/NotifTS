package usth.intern.notifts.ui.manager

import usth.intern.notifts.data.db.Notification

data class ManagerUiState (
    val notificationList: List<Notification> = listOf(),
    val query: String = "",
    val isAppDialogShown: Boolean = false
)
