package usth.intern.notifts.ui.manager.components

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import usth.intern.notifts.data.db.Notification

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
