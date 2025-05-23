package usth.intern.notifts.ui.dashboard

import usth.intern.notifts.data.db.NotificationCountByDate
import java.time.LocalDate

data class DashboardUiState(
    val notificationCountByDate: Map<LocalDate, Number> = mapOf()
)
