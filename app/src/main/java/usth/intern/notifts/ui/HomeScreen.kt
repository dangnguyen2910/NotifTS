package usth.intern.notifts.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import usth.intern.notifts.data.db.Notification

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

    val uiState by homeViewModel.uiState.collectAsState()
    val notification = uiState.notification

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Hello"
        )

        Switch(
            checked = uiState.isActivated,
            onCheckedChange = { homeViewModel.onSwitchClicked() }
        )

        NotificationText(notification)
    }
}

@Composable
fun NotificationText(
    notification: Notification?,
    modifier: Modifier = Modifier
) {
    val title = notification?.title ?: ""
    val text = notification?.text ?: ""

    Text(text = title, modifier = modifier)
    Text(text = text, modifier = modifier)
}