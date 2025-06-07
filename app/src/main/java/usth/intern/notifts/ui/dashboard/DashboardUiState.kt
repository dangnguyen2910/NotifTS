package usth.intern.notifts.ui.dashboard

import java.time.LocalDate

data class DashboardUiState(
    val notificationCountByDate: Map<LocalDate, Number> = mapOf(),
    val notificationCountByApp: Map<String, Number> = mapOf(),
)
