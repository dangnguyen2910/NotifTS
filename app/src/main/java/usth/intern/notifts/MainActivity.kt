package usth.intern.notifts

import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import usth.intern.notifts.ui.theme.NotifTSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotifTSTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                        NotifTSApp()
                }
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.isNotificationListenerAccessGranted(
                ComponentName(this, NotificationToSpeechService::class.java)
            )) {
                askForPermission()
            }
        }
    }

    // TODO: check if app has access to notification listener
    private fun hasAccessToListenNotification(): Boolean {
        return true
    }

    // TODO: Open notification access window
    private fun askForPermission() {
        try {
            val settingsIntent =
                Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(settingsIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}

@Composable
fun NotifTSApp(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Hello"
        )
    }
}