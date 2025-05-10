package usth.intern.notifts.ui.manager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun Apps(
    appList: List<String>,
    onDismissRequest: () -> Unit,
    updateAppFilterSelections: (String) -> Unit,
    onCancelAppFilter: () -> Unit,
    onConfirmAppFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checkedList = remember(appList) { List(appList.size) { mutableStateOf(false) } }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(475.dp),
            shape = RoundedCornerShape(7.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // Apps list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(appList) { index, app ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(app)
                            Spacer(modifier = modifier.weight(1f))
                            Checkbox(
                                checked = checkedList[index].value,
                                onCheckedChange = {
                                    updateAppFilterSelections(app)
                                    checkedList[index].value = it
                                }
                            )
                        }
                        HorizontalDivider()
                    }
                }
                // Confirm and Cancel buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            onCancelAppFilter()
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onConfirmAppFilter()
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

@Composable
fun Category(
    categoryList: List<String?>,
    onDismissRequest: () -> Unit,
    updateCategoryFilterSelections: (String?) -> Unit,
    onCancelCategoryFilter: () -> Unit,
    onConfirmCategoryFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checkedList = remember(categoryList) { List(categoryList.size) { mutableStateOf(false) } }

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(475.dp),
            shape = RoundedCornerShape(7.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
//                    .weight(1f)
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // Option list
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(categoryList) { index, category ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (category == null || category == "") {
                                Text("Unknown")
                            } else {
                                Text(category)
                            }
                            Spacer(modifier = modifier.weight(1f))
                            Checkbox(
                                checked = checkedList[index].value,
                                onCheckedChange = {
                                    updateCategoryFilterSelections(category)
                                    checkedList[index].value = it
                                }
                            )
                        }
                        HorizontalDivider()
                    }
                }
                // Confirm and Cancel buttons.
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            onCancelCategoryFilter()
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onConfirmCategoryFilter()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Date(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Select date range"
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    Apps(
        appList = listOf("App1", "App2", "App3", "App4"),
        onDismissRequest = {},
        updateAppFilterSelections = {},
        onCancelAppFilter = {},
        onConfirmAppFilter = {},
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryPreview() {
    Category(
        categoryList = listOf("alarm", "msg","", "", "", "", "", "", ""),
        onDismissRequest = {},
        updateCategoryFilterSelections = {},
        onCancelCategoryFilter = {},
        onConfirmCategoryFilter = {},
    )
}

@Preview(showBackground = true)
@Composable
fun DatePreview() {
    Date(
        onDateRangeSelected = {},
        onDismiss = {}
    )
}
