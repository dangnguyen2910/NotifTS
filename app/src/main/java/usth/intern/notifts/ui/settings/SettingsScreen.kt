package usth.intern.notifts.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OptionBlock("GENERAL") {
            OptionWithSwitch(
                optionName = "Enable speaker",
                isActivated = uiState.isActivated,
                activateFunction = { settingsViewModel.enableSpeaker() },
            )
            HorizontalDivider()
            OptionWithSwitch(
                optionName = "Enable speaker when screen is on",
                isActivated = uiState.speakerIsEnabledWhenScreenOn,
                activateFunction = { settingsViewModel.enableSpeakerWhenScreenOn() }
            )
        }

        OptionBlock("TTS CONFIG") {
            Option(
                optionName = "Choose local TTS model",
                functionality = {/*TODO*/}
            )
            HorizontalDivider()
            Option(
                optionName = "Select voices",
                functionality = {/*TODO*/}
            )
        }

        OptionBlock("FILTERING") {
            Option(
                optionName = "Select apps",
                functionality = {/*TODO*/}
            )
            HorizontalDivider()
            Option(
                optionName = "Ignore prohibited words",
                functionality = {/*TODO*/}
            )
        }

        OptionBlock("TESTING") {
            Option(
                optionName = "Test local TTS model",
                functionality = {/*TODO*/}
            )
            HorizontalDivider()
            Option(
                optionName = "Test remote TTS model",
                functionality = {/*TODO*/}
            )
        }
    }
}

@Composable
fun OptionWithSwitch(
    optionName: String,
    isActivated: Boolean,
    activateFunction: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            activateFunction(isActivated)
        }
    ) {
        Text(
            text = optionName,
            fontSize = 17.sp,
            modifier = modifier
                .weight(0.9f)
        )

        Switch(
            checked = isActivated,
            onCheckedChange = activateFunction,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OptionWithSwitchPreview() {
    OptionWithSwitch(
        optionName = "Enable speaker",
        isActivated = true,
        activateFunction = {}
    )
}

@Composable
fun OptionBlock(
    optionCategory: String,
    optionList: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = optionCategory,
            color = Color(0xFF6C6C6C),
            fontSize = 12.sp
        )
        Spacer(Modifier.height(3.dp))
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE7E7E7)
            ),
        ) {
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                optionList()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptionBlockPreview() {
    OptionBlock(
        optionCategory = "GENERAL",
        optionList = {
            OptionWithSwitch(
                optionName = "Enable speaker",
                isActivated = true,
                activateFunction = {}
            )
            HorizontalDivider()
            Option(
                optionName = "Select apps",
                functionality = {},
            )
        }
    )
}

@Composable
fun Option(
    optionName: String,
    functionality: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .clickable { functionality() }
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = optionName,
            fontSize = 17.sp,
        )

    }
}

@Composable
@Preview(showBackground = true)
fun OptionPreview() {
    Option(
        optionName = "Select apps",
        functionality = {},
    )
}