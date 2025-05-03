package usth.intern.notifts.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import usth.intern.notifts.R
import usth.intern.notifts.data.db.Notification

@Composable
fun ManagerScreen(
    modifier: Modifier = Modifier,
) {
    val managerViewModel: ManagerViewModel = hiltViewModel<ManagerViewModel>()
    val uiState by managerViewModel.uiState.collectAsState()

    ManagerContent(
        query = uiState.query,
        notificationList = uiState.notificationList,
        onTypingSearch = { managerViewModel.onTypingSearch(it) },
        onReload = { managerViewModel.onReload() },
        onEnterSearch = { managerViewModel.onEnterSearch(it) },
        modifier = modifier
    )
}

@Composable
fun ManagerContent(
    // Search bar
    query: String,
    onTypingSearch: (String) -> Unit,
    onEnterSearch: (String) -> Unit,
    // Notification list
    notificationList: List<Notification>,
    onReload: () -> Unit,
    modifier: Modifier = Modifier,
    // Filter button
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val focusState = remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            KeywordsSearchBar(
                query = query,
                onQueryChange = onTypingSearch,
                onSearch = onEnterSearch,
                keyboardController = keyboardController,
                focusRequester = focusRequester,
                focusManager = focusManager,
                focusState = focusState,
            )

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
                            text = { Text("App") },
                            onClick = {/*todo*/}
                        )
                        DropdownMenuItem(
                            text = { Text("Category") },
                            onClick = {/*todo*/}
                        )
                        DropdownMenuItem(
                            text = { Text("Date") },
                            onClick = {/*todo*/}
                        )
                        DropdownMenuItem(
                            text = { Text("Clear") },
                            onClick = {/*todo*/}
                        )
                    }
                }
            }
        }

        Button(
            onClick = onReload,
        ) {
            Text("Reload")
        }

        NotificationCardList(notificationList)
    }
}

@Composable
fun FilterMenu() {

}

@Composable
fun KeywordsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = { Text("Search") },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Search, contentDescription = "Search") },
    keyboardController:SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    focusRequester: FocusRequester = FocusRequester(),
    focusManager: FocusManager = LocalFocusManager.current,
    focusState: MutableState<Boolean> = mutableStateOf(false)

) {
    //todo: Fix height
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(query)
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        shape = RoundedCornerShape(10.dp),
        leadingIcon = leadingIcon,
        placeholder = placeholder,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth(0.85f)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState.value = it.isFocused }
            .focusable()
            .height(50.dp)
        )
}

@Composable
fun NotificationCard(
    packageName: String,
    title: String,
    text: String,
    date: String,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp, horizontal = 10.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(all = 5.dp)
        ) {
            Row {
                Text(packageName)
                Spacer(modifier = modifier.weight(1f))
                Text(date)
            }
            HorizontalDivider()
            Text(title)
            HorizontalDivider()
            Text(text)
        }
    }
}

@Composable
fun NotificationCardList(
    notificationList: List<Notification>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items (notificationList){ notification ->
            NotificationCard(
                packageName = notification.packageName,
                title = notification.title,
                text = notification.text,
                date = notification.date
            )
            Spacer(modifier = modifier.height(0.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotificationCardPreview() {
    NotificationCard(
        packageName = "com.preview.whatever",
        title = "This is a title",
        text = "In Jetpack Compose, you can create a horizontal rule (a horizontal divider or line) " +
                "using the Divider composable, which is part of the Material design components. " +
                "The Divider composable allows you to draw a simple line that spans across the " +
                "width" + " of its parent container.",
        date = "30/30/3030 11h30"
    )
}

@Preview(showBackground = true)
@Composable
fun ManagerScreenPreview() {
    ManagerContent(
        notificationList = listOf(
            Notification(
                rowid = 0,
                packageName = "com.preview.whatever",
                title = "This is a title",
                text = "In Jetpack Compose, you can create a horizontal rule (a horizontal divider or line) " +
                        "using the Divider composable, which is part of the Material design components. " +
                        "The Divider composable allows you to draw a simple line that spans across the " +
                        "width" + " of its parent container.",
                bigText = null,
                category = null,
                date = "30/30/3030 11h30"
            ),

            Notification(
                rowid = 0,
                packageName = "com.preview.whatever",
                title = "This is a title",
                text = "In Jetpack Compose, you can create a horizontal rule (a horizontal divider or line) " +
                        "using the Divider composable, which is part of the Material design components. " +
                        "The Divider composable allows you to draw a simple line that spans across the " +
                        "width" + " of its parent container.",
                bigText = null,
                category = null,
                date = "30/30/3030 11h30"
            ),
        ),
        onReload = {},
        onTypingSearch = { _: String -> },
        onEnterSearch = { _: String -> },
        query = ""
    )
}
