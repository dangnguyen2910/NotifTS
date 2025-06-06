package usth.intern.notifts.ui.settings

data class SettingsUiState(
    val isActivated: Boolean,
    val speakerIsEnabledWhenScreenOn: Boolean,
    val speakerIsEnabledWhenDndOn: Boolean,
    val notificationIsShown: Boolean,
    val showVoiceDialog: Boolean = false,
    val englishVoiceList: List<String> = listOf("Heart (F)", "Bella (F)"),
    val currentEnglishVoice: String,
)
