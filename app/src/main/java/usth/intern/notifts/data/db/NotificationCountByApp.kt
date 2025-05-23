package usth.intern.notifts.data.db

data class NotificationCountByApp(
    val app: String,
    val notificationCount: Int
)