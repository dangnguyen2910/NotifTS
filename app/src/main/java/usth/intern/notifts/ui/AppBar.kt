package usth.intern.notifts.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import usth.intern.notifts.NotiftsScreen
import usth.intern.notifts.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Title(
    title: String,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1
            )
        },
        modifier = modifier
    )
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var inSettingsScreen by remember { mutableStateOf(true) }
    var inManagerScreen by remember { mutableStateOf(false) }
//    val inDashboardScreen by remember { mutableStateOf(false) }

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = inSettingsScreen,
            onClick = {
                navController.navigate(NotiftsScreen.Settings.name)
                inSettingsScreen = true
                inManagerScreen = false
            }
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
            onClick = {
                navController.navigate(NotiftsScreen.Manager.name)
                inSettingsScreen = false
                inManagerScreen = true
            }
        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Settings, contentDescription = "Dashboard") },
//            label = { Text("Dashboard") },
//            selected = inDashboardScreen,
//            onClick = { navController.navigate(NotiftsScreen.DashBoard.name)}
//        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        navController = rememberNavController(),
    )
}