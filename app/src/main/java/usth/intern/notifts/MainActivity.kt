package usth.intern.notifts

import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import usth.intern.notifts.domain.NotificationListener
import usth.intern.notifts.ui.settings.SettingsViewModel
import usth.intern.notifts.ui.theme.NotiftsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotiftsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NotiftsScreen(settingsViewModel)
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

        settingsViewModel.openTtsSetting.observe(this) {
            try {
                val ttsSettingsIntent = Intent("com.android.settings.TTS_SETTINGS")
                startActivity(ttsSettingsIntent)
            } catch (e: ActivityNotFoundException) {
                Log.e("MainActivity", "TTS settings not found")
            }
        }

    }

}
