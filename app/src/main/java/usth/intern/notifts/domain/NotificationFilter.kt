package usth.intern.notifts.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import usth.intern.notifts.data.db.Notification
import usth.intern.notifts.data.repository.PreferenceRepository
import javax.inject.Singleton

class NotificationFilter(
    private val preferenceRepository: PreferenceRepository,
    private val ignoredAppList: List<String>
) {

    fun filter(notification: Notification) : Boolean {
        return (
            checkIgnoredNotification(notification) ||
            checkEmptyContent(notification) ||
            checkDuplication(notification.text)
        )
    }

    private fun checkIgnoredNotification(notification: Notification) : Boolean {
        return notification.packageName in ignoredAppList
    }

    private fun checkEmptyContent(notification: Notification) : Boolean {
        return (notification.text == "" || notification.title == "")
    }

    private fun checkDuplication(text: String) : Boolean {
        var previousNotificationText = ""
        runBlocking {
            launch {
                previousNotificationText = preferenceRepository.previousNotificationText.first()
            }
        }
        return previousNotificationText == text
    }
}