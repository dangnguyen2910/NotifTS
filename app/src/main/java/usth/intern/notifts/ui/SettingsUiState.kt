package usth.intern.notifts.ui

data class SettingsUiState(
    val isActivated: Boolean,
    val speakerIsEnabledWhenScreenOn: Boolean,
    val speakerIsEnabledWhenDndOn: Boolean,
    val notificationIsShown: Boolean,
)
