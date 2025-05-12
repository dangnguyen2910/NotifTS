package usth.intern.notifts

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import usth.intern.notifts.ui.BottomNavigationBar
import usth.intern.notifts.ui.SettingsScreen
import usth.intern.notifts.ui.Title
import usth.intern.notifts.ui.manager.ManagerScreen

enum class NotiftsScreen(val title: String) {
    Settings(title = "Settings"),
    Manager(title = "Notification Manager"),
    DashBoard(title = "Dashboard"),
}

@Composable
fun NotiftsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Get the name of the current screen
    val currentScreen = NotiftsScreen.valueOf(
        backStackEntry?.destination?.route ?: NotiftsScreen.Settings.name
    )


    Scaffold(
        topBar = { Title(title = currentScreen.title) },
        bottomBar = { BottomNavigationBar(
            navController = navController,
        ) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NotiftsScreen.Settings.name,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            modifier = modifier
        ) {
            composable(route = NotiftsScreen.Settings.name) {
                SettingsScreen(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable(route = NotiftsScreen.Manager.name) {
                ManagerScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
