package usth.intern.notifts.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    val activationStateList: List<Boolean> = listOf(
        uiState.isActivated,
        uiState.speakerIsEnabledWhenScreenOn,
        uiState.speakerIsEnabledWhenDndOn,
        uiState.notificationIsShown,
    )

    val activateFunctionList: List<(Boolean) -> Unit> = listOf(
        { settingsViewModel.onIsActivatedSwitchClicked() },
        { settingsViewModel.onScreenOnSwitchClicked() },
    )

    SettingsContent(
        activationStateList = activationStateList,
        activateFunction = activateFunctionList,
        modifier = modifier
    )

}

@Composable
fun SettingsContent(
    activationStateList: List<Boolean>,
    activateFunction: List<(Boolean) -> Unit>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        Option(
            optionText = "Enable speaker",
            isActivated = activationStateList[0],
            activateFunction = activateFunction[0],
        )

        Option(
            optionText = "Enable speaker when screen is on",
            isActivated = activationStateList[1],
            activateFunction = activateFunction[1],
        )
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
            modifier = modifier
                .padding(start = 6.dp)
                .weight(0.9f)
        )

        Switch(
            checked = isActivated,
            onCheckedChange = activateFunction,
            modifier = modifier.padding(end = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsContent(
        activationStateList = listOf(true, true, true, true),
        activateFunction = listOf({}, {}, {}, {}),
    )
}