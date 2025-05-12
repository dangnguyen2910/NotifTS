package usth.intern.notifts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import usth.intern.notifts.domain.dashboard.Dashboard

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

@Preview(showBackground = true)
@Composable
fun DashboardContentPreview() {
    DashboardContent()
}