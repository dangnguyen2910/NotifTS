package usth.intern.notifts.ui

import usth.intern.notifts.data.db.Notification

data class ManagerUiState (
    val notificationList: List<Notification> = listOf()
)
