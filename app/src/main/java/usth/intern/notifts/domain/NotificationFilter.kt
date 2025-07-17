package usth.intern.notifts.domain

import android.util.Log
import usth.intern.notifts.data.db.Notification

class NotificationFilter(
    private val ignoredAppList: List<String>,
    private val previousNotification: Notification?
) {

    fun filter(notification: Notification) : Boolean {
        return (
            checkIgnoredNotification(notification) ||
            checkEmptyContent(notification) ||
            checkDuplication(notification, previousNotification)
        )
    }

    private fun checkIgnoredNotification(notification: Notification) : Boolean {
        if (notification.packageName in ignoredAppList){
            Log.e("NotificationFilter",
                "The notification from ${notification.packageName} is ignored.")
            return true
        } else return false
    }

    private fun checkEmptyContent(notification: Notification) : Boolean {
        if (notification.text == "" || notification.title == "") {
            Log.e("NotificationFilter",
                "This notification doesn't have content.")
            return true
        } else return false
    }

    private fun checkDuplication(notification: Notification, previousNotification: Notification?) : Boolean {
        if (previousNotification == null) {
            return false
        }

        val deltaTime = notification.timestamp - previousNotification.timestamp
        Log.d("NotificationFilter", "Delta time: $deltaTime")
        if (notification.text == previousNotification.text && deltaTime <= 5 * 1000) {
            Log.e("NotificationFilter",
                "This notification is duplication.")
            return true
        } else return false
    }
}