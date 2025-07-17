package usth.intern.notifts.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import usth.intern.notifts.ui.theme.white

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
                    .selectableGroup()
                    .padding(16.dp),
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

@Composable
fun TestDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (Pair<String, String>) -> Unit,
    onCancel: () -> Unit,
    modifier:Modifier = Modifier,
) {
    var title by remember { mutableStateOf("Test notification.") }
    var text by remember { mutableStateOf("This is a notification for testing.") }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(230.dp),
            shape = RoundedCornerShape(7.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Push a notification for testing.")
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = white,
                        unfocusedContainerColor = white,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = white,
                        unfocusedContainerColor = white,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                )
                // Confirm and Cancel buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            onCancel()
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onConfirm(Pair(title, text))
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestDialogPreview() {
    TestDialog({}, {}, {})
}