package usth.intern.notifts.ui.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import usth.intern.notifts.R

@Composable
fun FilterScreen() {
    var expanded by remember { mutableStateOf(false) }
    val filterViewModel = hiltViewModel<FilterViewModel>()
    val uiState by filterViewModel.uiState.collectAsState()

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .height(55.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Icon(
                    painter = painterResource(R.drawable.filter_icon),
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .size(35.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            ) {
                DropdownMenuItem(
                    text = { Text("Apps") },
                    onClick = { filterViewModel.onClickAppFilterButton() }
                )
                DropdownMenuItem(
                    text = { Text("Category") },
                    onClick = { filterViewModel.onClickCategoryFilterButton() }
                )
                DropdownMenuItem(
                    text = { Text("Date") },
                    onClick = { filterViewModel.onClickDateFilterButton() }
                )
                DropdownMenuItem(
                    text = { Text("Clear") },
                    onClick = {/*TODO: Implement the function of Clear button */}
                )
            }

            when {
                uiState.appFilterDialogShown -> Apps(
                    appList = listOf(),
                    onDismissRequest = {filterViewModel.onDismissAppFilterDialog()}
                )
                uiState.categoryFilterDialogShown -> Category(
                    categoryList = uiState.categoryList,
                    onDismissRequest = { filterViewModel.onDismissCategoryFilterDialog() }
                )
                uiState.dateFilterDialogShown -> Date(
                    //TODO: here lies a function that input is a pair of date. It will change the
                    // UI state (list of notifications) of manager screen based on the input.
                    onDateRangeSelected = {},
                    onDismiss= { filterViewModel.onDismissDateFilterDialog() }
                )
            }
        }
    }
}

@Composable
fun Apps(
    appList: List<String>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO: checked is for box, in case there is more than 1 box then this will break
    var checked by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(475.dp),
            shape = RoundedCornerShape(7.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(appList) { app ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(app)
                        Spacer(modifier = modifier.weight(1f))
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { checked = it }
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun Category(
    categoryList: List<String?>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO: checked is for box, in case there is more than 1 box then this will break
    var checked by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(475.dp),
            shape = RoundedCornerShape(7.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categoryList) { category ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (category != null) {
                            Text(category)
                        } else {
                            Text("Unknown")
                        }
                        Spacer(modifier = modifier.weight(1f))
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { checked = it }
                        )
                    }
                    HorizontalDivider()
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
                Text("OK")
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
        onDismissRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryPreview() {
    Category(
        categoryList = listOf("alarm", "msg"),
        onDismissRequest = {},
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
