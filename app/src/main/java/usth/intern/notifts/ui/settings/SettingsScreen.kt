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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
) {
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
                functionality = { settingsViewModel.chooseLocalTtsModel() }
            )
            HorizontalDivider()
            Option(
                optionName = "Select voices for external model",
                functionality = { settingsViewModel.onClickVoiceSelection() }
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

        if (uiState.showVoiceDialog) {
            VoiceSelectionDialog(
                onDismissRequest = { settingsViewModel.onDismissVoiceSelection() },
                englishVoiceList = uiState.englishVoiceList,
                currentEnglishVoice = uiState.currentEnglishVoice,
                onEnglishVoiceSelected = { settingsViewModel.onEnglishVoiceSelected(it) },
                frenchVoiceList = uiState.frenchVoiceList,
                currentFrenchVoice = uiState.currentFrenchVoice,
                onFrenchVoiceSelected = {/*TODO: do this when there is more french voice*/}
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
    modifier: Modifier = Modifier,
    functionality: () -> Unit = {},
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

@Composable
fun VoiceSelectionDialog(
    englishVoiceList: List<String>, 
    currentEnglishVoice: String, 
    onEnglishVoiceSelected: (String) -> Unit,
    frenchVoiceList: List<String>,
    currentFrenchVoice: String,
    onFrenchVoiceSelected: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(475.dp),
            shape = RoundedCornerShape(7.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                VoiceSelectionBlock(
                    language = "English",
                    voiceList = englishVoiceList,
                    currentVoice = currentEnglishVoice,
                    onVoiceSelected = onEnglishVoiceSelected 
                )
                VoiceSelectionBlock(
                    language = "French",
                    voiceList = frenchVoiceList,
                    currentVoice = currentFrenchVoice,
                    onVoiceSelected = onFrenchVoiceSelected,
                )
            }

//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .selectableGroup(),
//                verticalArrangement = Arrangement.SpaceBetween,
//            ) {
//                Text("French")
//            }
        }
    }
}

@Composable
fun VoiceSelectionBlock(
    language: String,
    voiceList: List<String>,
    currentVoice: String,
    onVoiceSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
//    val englishVoice = listOf("Heart (F)", "Bella (F)")
//    val (selectedOption, onOptionSelected) = remember { mutableStateOf(englishVoice[0]) }

    Text(language)
    voiceList.forEach { voice ->
        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .selectable(
                    selected = (voice == currentVoice),
                    onClick = { onVoiceSelected(voice) },
                    role = Role.RadioButton
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (voice == currentVoice),
                onClick = null
            )
            Text(
                text = voice,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
@Composable
@Preview(showBackground = true)
fun VoiceSelectionDialogPreview() {
    VoiceSelectionDialog(
        onDismissRequest = {},
        englishVoiceList = listOf("one", "two"),
        currentEnglishVoice = "one",
        onEnglishVoiceSelected = {},
        frenchVoiceList = listOf("French voice"),
        currentFrenchVoice = "French voice",
        onFrenchVoiceSelected = {}
    )
}