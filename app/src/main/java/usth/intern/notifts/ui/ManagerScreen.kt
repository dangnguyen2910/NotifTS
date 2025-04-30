package usth.intern.notifts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ManagerScreen(
    modifier: Modifier = Modifier
) {
    ManagerContent(modifier)
}

@Composable
fun ManagerContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text("hello")
    }
}

@Preview(showBackground = true)
@Composable
fun ManagerScreenPreview() {
    ManagerContent()
}
