package usth.intern.notifts.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import usth.intern.notifts.R
import usth.intern.notifts.ui.dashboard.component.JetpackComposeBasicColumnChart
import usth.intern.notifts.ui.dashboard.component.JetpackComposeBasicLineChart
import usth.intern.notifts.ui.theme.green
import usth.intern.notifts.ui.theme.white


@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>()
    val uiState by dashboardViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        dashboardViewModel.getNotificationCountByDay()
        dashboardViewModel.getNotificationCountByApp()
    }

    Column (
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GraphCard(
            title = "Notification Count by Days",
            onClick = { dashboardViewModel.getNotificationCountByDay() },
            graph = { JetpackComposeBasicLineChart(
                countMap = uiState.notificationCountByDate
            ) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        GraphCard(
            title = "Notification Count by Apps",
            onClick = { dashboardViewModel.getNotificationCountByApp() },
            graph = { JetpackComposeBasicColumnChart(
                countByAppMap = uiState.notificationCountByApp
            ) }
        )
    }
}

@Composable
fun GraphCard(
    title: String,
    onClick: () -> Unit,
    graph: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column (
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(0.9f),
                    fontSize = 20.sp
                )
                RefreshButton(
                    onClick = onClick,
                    modifier = Modifier.weight(0.1f)
                )
            }
            HorizontalDivider()
            graph()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GraphCardPreview() {
    GraphCard("Title", {}, graph = {})
}

@Composable
fun RefreshButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = green
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .size(40.dp)
            .padding(vertical = 3.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.refresh_icon),
            contentDescription = stringResource(R.string.text_filter_icon),
            tint = white,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RefreshButtonPreview() {
    RefreshButton({})
}