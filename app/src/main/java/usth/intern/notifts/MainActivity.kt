package usth.intern.notifts

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import usth.intern.notifts.data.repository.AppStatusRepository
import usth.intern.notifts.data.repository.PreferenceRepository
import usth.intern.notifts.domain.AppStatus
import usth.intern.notifts.domain.NotificationListener
import usth.intern.notifts.ui.settings.SettingsViewModel
import usth.intern.notifts.ui.theme.NotiftsTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var appStatusRepository: AppStatusRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
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

        settingsViewModel.shouldPushNotification.observe(this) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
                )
            }

            Log.d("MainActivity", "Push test notification.")
            val title = settingsViewModel.uiState.value.testNotificationTitle
            val text = settingsViewModel.uiState.value.testNotificationText
            sendNotification(this, title, text)
        }

        CoroutineScope(Dispatchers.IO).launch {
            if (preferenceRepository.isFirstTime.first()) {
                val appList = getAppList()
                appList.forEach { app ->
                    appStatusRepository.insertAppStatus(AppStatus(appName = app, status = true))
                }
                preferenceRepository.updateIsFirstTime()
            }
        }


    }

    private fun getAppList(): MutableList<String> {
        val appList: MutableList<String> = mutableListOf()

        val packageManager = this@MainActivity.packageManager
        val packages = packageManager.getInstalledPackages(0)

        for (packageInfo in packages) {
            val appName = packageInfo.applicationInfo?.let {
                packageManager.getApplicationLabel(it).toString()
            }
            if (appName != null) {
                appList.add(appName)
            }
        }
        appList.sort()
        return appList
    }

    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            "notifts",
            "NotifTS Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun sendNotification(context: Context, title: String, text: String) {
        val builder = NotificationCompat.Builder(context, "notifts")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1001, builder.build()) // 1001 = notification ID
    }

}
