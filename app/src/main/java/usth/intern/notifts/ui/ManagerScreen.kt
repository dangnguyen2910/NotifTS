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
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
        notificationList = uiState.notificationList,
        onReload = { managerViewModel.onReload() },
        modifier = modifier
    )
}

@Composable
fun ManagerContent(
    notificationList: List<Notification>,
    onReload: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column (modifier = modifier) {
        Button(
            onClick = onReload,
        ) {
            Text("Reload")
        }
        NotificationCardList(notificationList)
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
        onReload = {}
    )
}
