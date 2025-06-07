package usth.intern.notifts.domain

data class NotificationCountByApp(
    val app: String,
    val notificationCount: Int
)