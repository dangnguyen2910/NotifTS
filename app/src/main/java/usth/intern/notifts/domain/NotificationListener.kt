package usth.intern.notifts.domain

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import usth.intern.notifts.data.TAG
import usth.intern.notifts.domain.tts.TtsEngine
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    @Inject
    lateinit var engine: TtsEngine

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