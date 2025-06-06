package usth.intern.notifts.domain

data class NotificationPackage(
    val app: String,
    val title: String,
    val text: String,
    val language: String,
    val voice: String,
)
