package usth.intern.notifts.ui.dashboard

import usth.intern.notifts.data.db.NotificationCountByDate

data class DashboardUiState(
    val notificationCountByDate: Map<String, Number> = mapOf()
)
