package usth.intern.notifts.ui.manager.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NotificationCard(
    packageName: String,
    title: String,
    text: String,
    date: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(all = 5.dp)
        ) {
            Row {
                Text(packageName)
                Spacer(modifier = modifier.weight(1f))
                Text(date)
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Text(title)
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Text(text)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotificationCardPreview() {
    val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    NotificationCard(
        packageName = "com.preview.whatever",
        title = "This is a title",
        text = "In Jetpack Compose, you can create a horizontal rule (a horizontal divider or line) " +
                "using the Divider composable, which is part of the Material design components. " +
                "The Divider composable allows you to draw a simple line that spans across the " +
                "width" + " of its parent container.",
        date = formatter.format(java.util.Date().time)
    )
}
