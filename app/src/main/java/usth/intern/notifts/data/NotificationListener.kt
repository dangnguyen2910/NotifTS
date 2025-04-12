package usth.intern.notifts.data

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "NotificationToSpeechService"

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    @Inject
    lateinit var engine: Engine

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Connected to notification to speech service")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d(TAG, "Disconnect to notification to speech service")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        engine.run(sbn)
    }
}