package usth.intern.notifts.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import usth.intern.notifts.R
import usth.intern.notifts.ui.theme.annapolosBlue
import usth.intern.notifts.ui.theme.cottonBall
import usth.intern.notifts.ui.theme.eveningGlory
import usth.intern.notifts.ui.theme.lightSilver

@Composable
fun Title(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = eveningGlory,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TitlePreview() {
    Title("Settings")
}

@Composable
fun BottomNavigationBar(
    onClickSettingsButton: () -> Unit,
    onClickSearchFilterButton: () -> Unit,
    onClickDashboardButton: () -> Unit,
    currentScreen: String,
    modifier: Modifier = Modifier
) {

    var inSettingsScreen = false
    var inManagerScreen = false
    var inDashboardScreen = false

    when (currentScreen) {
        "Settings" -> inSettingsScreen = true
        "Notification Manager" -> inManagerScreen = true
        "Dashboard" -> inDashboardScreen = true
    }

    NavigationBar(containerColor = cottonBall) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = inSettingsScreen,
            onClick = onClickSettingsButton,
            colors = NavigationBarItemColors(
                selectedIconColor = annapolosBlue,
                selectedTextColor = Color.Unspecified,
                selectedIndicatorColor = Color.Transparent,
                unselectedIconColor = lightSilver,
                unselectedTextColor = lightSilver,
                disabledIconColor = Color.Unspecified,
                disabledTextColor = Color.Unspecified
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painterResource(R.drawable.manager_icon),
                    contentDescription = "Manager",
                )
            },
            label = { Text("Search & Filter") },
            selected = inManagerScreen,
            onClick = onClickSearchFilterButton,
            colors = NavigationBarItemColors(
                selectedIconColor = annapolosBlue,
                selectedTextColor = Color.Unspecified,
                selectedIndicatorColor = Color.Transparent,
                unselectedIconColor = lightSilver,
                unselectedTextColor = lightSilver,
                disabledIconColor = Color.Unspecified,
                disabledTextColor = Color.Unspecified
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painterResource(R.drawable.dashboard_icon),
                    contentDescription = "Dashboard"
                )
            },
            label = { Text("Dashboard") },
            selected = inDashboardScreen,
            onClick = onClickDashboardButton,
            colors = NavigationBarItemColors(
                selectedIconColor = annapolosBlue,
                selectedTextColor = Color.Unspecified,
                selectedIndicatorColor = Color.Transparent,
                unselectedIconColor = lightSilver,
                unselectedTextColor = lightSilver,
                disabledIconColor = Color.Unspecified,
                disabledTextColor = Color.Unspecified
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        currentScreen = "Settings",
        onClickSettingsButton = {},
        onClickSearchFilterButton = {},
        onClickDashboardButton = {},
    )
}