package usth.intern.notifts.domain

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import usth.intern.notifts.data.PreferenceRepository
import usth.intern.notifts.domain.tts.TtsEngine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val TAG = "NotificationListener"

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    @Inject
    lateinit var ttsEngine: TtsEngine

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    private var previousText: String = ""

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

        Log.d(TAG, "-----------------------------------------------------")
        Log.d(TAG, sbn.packageName)

        val category = sbn.notification.category
        Log.d(TAG, "Category: $category")

        val title = sbn.notification.extras.getCharSequence("android.title") ?: ""
        Log.d(TAG, "Title: $title")

        val text = sbn.notification.extras.getCharSequence("android.text") ?: ""
        Log.d(TAG, "Text: $text")
        Log.d(TAG, "Previous text: $previousText")

        val bigText = sbn.notification.extras.getCharSequence("android.bigText")?: ""
        Log.d(TAG, "Big Text: $bigText")

        val dateString = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            .format(Date(sbn.postTime))
        Log.d(TAG, "Date: $dateString")

        runBlocking {
            launch {
                val isActivated = preferenceRepository.isActivatedFlow.first()

                if (isActivated && text != previousText) {
                    previousText = text.toString()
                    ttsEngine.run(sbn)
                }
            }
        }

    }
}