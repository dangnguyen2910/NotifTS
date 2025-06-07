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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AppListScreen(
    modifier: Modifier = Modifier
) {
    val appListViewModel = hiltViewModel<AppListViewModel>()
    val appList = appListViewModel.getAppList()

    LazyColumn(modifier = modifier.fillMaxSize().padding(16.dp)) {
        itemsIndexed(appList) { index, appName ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(appName, modifier = Modifier.weight(0.9f))
                Checkbox(
                    checked = appListViewModel.getAppStatus(),
                    onCheckedChange = {
                        appListViewModel.updateSelection(appName)
                    },
                )
            }
        }
    }
}