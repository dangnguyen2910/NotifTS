package usth.intern.notifts.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {
    DashboardContent(
        modifier = modifier
    )
}

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // TODO: Implement me
    }
}

@Composable
fun StatisticsCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(5.dp)
    ) {
        Column {
            Text("Statistics")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsCardPreview() {
    StatisticsCard()
}

@Preview(showBackground = true)
@Composable
fun DashboardContentPreview() {
    DashboardContent()
}