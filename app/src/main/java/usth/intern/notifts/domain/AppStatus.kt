package usth.intern.notifts.domain

data class AppStatus(
    val rowid: Long? = null,
    val appName: String,
    var status: Boolean
)
