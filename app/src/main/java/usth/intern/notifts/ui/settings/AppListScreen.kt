package usth.intern.notifts.ui.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AppListScreen(
    modifier: Modifier = Modifier
) {
    val appListViewModel = hiltViewModel<AppListViewModel>()
    val uiState = appListViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        appListViewModel.getAppStatusList()
        appListViewModel.getStatusList()
    }
    val appList = uiState.value.appStatusList
    val statusList = uiState.value.statusList

    if (appList.isNotEmpty() && statusList.isNotEmpty() && appList.size == statusList.size) {
        val checkedList = remember(statusList) { statusList.map { mutableStateOf(it) }}
        LazyColumn(modifier = modifier.fillMaxSize().padding(16.dp)) {
            itemsIndexed(appList) { index, appStatus ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(appStatus.appName, modifier = Modifier.weight(0.9f))
                    Checkbox(
                        checked = checkedList[index].value,
                        onCheckedChange = {
                            appListViewModel.updateSelection(appStatus)
                            checkedList[index].value = it
                        },
                    )
                }
            }
        }
    }

}