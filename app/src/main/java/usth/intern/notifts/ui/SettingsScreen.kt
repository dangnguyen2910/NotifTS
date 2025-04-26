package usth.intern.notifts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
) {

    val settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>()
    val uiState by settingsViewModel.uiState.collectAsState()

    SettingsContent(
        uiState.isActivated,
        { settingsViewModel.onSwitchClicked() }, 
        modifier
    )

}

@Composable
fun SettingsContent(
    isActivated: Boolean,
    activateFunction: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Title("Settings")

        Option(
            optionText = "Enable speaker",
            isActivated = isActivated,
            activateFunction = activateFunction,
        )

        Option(
            optionText = "Enable speaker when screen is on",
            isActivated = isActivated,
            activateFunction = activateFunction,
        )

        Option(
            optionText = "Enable speaker when Do Not Disturb\nmode is on",
            isActivated = isActivated,
            activateFunction = activateFunction,
        )

        Option(
            optionText = "Display a non-swipeable notification\nwhen the speaker is enabled",
            isActivated = isActivated,
            activateFunction = activateFunction,
        )
    }
    Column {
    }

}

@Composable
fun Option(
    optionText: String,
    isActivated: Boolean,
    activateFunction: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = optionText,
            fontSize = 17.sp,
            modifier = modifier.padding(start = 6.dp)
        )

        Spacer(modifier = modifier.weight(1f))

        Switch(
            checked = isActivated,
            onCheckedChange = activateFunction,
            modifier = modifier.padding(end = 6.dp)
        )
    }
}

@Composable
fun Title(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .background(Color.LightGray)
            .padding(top = 3.dp, bottom = 3.dp)

    ) {
        Text(
            text = title,
            fontSize = 26.sp,
            modifier = modifier.padding(start = 15.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsContent(
        isActivated = true,
        activateFunction = {}
    )
}