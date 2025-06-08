package usth.intern.notifts.ui.settings

import usth.intern.notifts.domain.AppStatus

data class AppListUiState(
    val appStatusList: MutableList<AppStatus> = mutableListOf(),
    val statusList: List<Boolean> = listOf()
)