package usth.intern.notifts

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import usth.intern.notifts.domain.NotificationListener
import usth.intern.notifts.ui.SettingsScreen
import usth.intern.notifts.ui.theme.NotifTSTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotifTSTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NotiftsScreen()
                }
            }

        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")

        val notificationIsAccessed = notificationManager.isNotificationListenerAccessGranted(
            ComponentName(this, NotificationListener::class.java)
        )

        if (!notificationIsAccessed) {
            startActivity(intent)
        }
    }

}
