package usth.intern.notifts.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
) {
    Column (modifier = modifier) {
        KeywordsSearchBar(
            query = query,
            onQueryChange = onTypingSearch,
            onSearch = onEnterSearch,
            searchResults = listOf(),
            onResultClick = { _: String -> }
        )

        Button(
            onClick = onReload,
        ) {
            Text("Reload")
        }

        NotificationCardList(notificationList)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeywordsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
//     Customization options
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = { Text("Search") },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Search, contentDescription = "Search") },
    trailingIcon: @Composable (() -> Unit)? = null,
//    supportingContent: (@Composable (String) -> Unit)? = null,
//    leadingContent: (@Composable () -> Unit)? = null,
) {
//    var expanded by rememberSaveable { mutableStateOf(false) }
    val expanded = false

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch(query)
                        //                    expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = {/*todo*/ },
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                )
            },
            expanded = expanded,
            onExpandedChange = {/*todo*/ },
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
                .fillMaxWidth(0.98f)
        ) {

        }
    }
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
