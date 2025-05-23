package usth.intern.notifts.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import usth.intern.notifts.ui.dashboard.component.JetpackComposeBasicLineChart


@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>()
    val uiState by dashboardViewModel.uiState.collectAsState()

    Column(modifier = modifier) {
        Button(onClick = {dashboardViewModel.getNotificationCountByDay()}) {
            Text("Refresh")
        }
        if (uiState.notificationCountByDate.isNotEmpty()) {
            JetpackComposeBasicLineChart(
                countMap = uiState.notificationCountByDate,
                updateNotificationCount = {}
            )
        }
    }
}

