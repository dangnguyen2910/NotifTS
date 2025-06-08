package usth.intern.notifts.data.repository

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import usth.intern.notifts.data.db.AppDatabase
import usth.intern.notifts.data.db.Notification
import usth.intern.notifts.domain.NotificationCountByApp
import usth.intern.notifts.domain.NotificationCountByDate
import java.util.Calendar
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val db = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "notification-database"
    ).fallbackToDestructiveMigration(dropAllTables = true).build()

    private val notificationDao = db.notificationDao()

    suspend fun insertNotification(
        packageName: String,
        title: String,
        text: String,
        bigText: String?,
        category: String?,
        date: Long
    ) : Long{
        val notification = Notification(
            packageName = packageName,
            title = title,
            text = text,
            bigText = bigText,
            category = category,
            timestamp = date
        )

        val id = notificationDao.insertNotification(notification)
        return id
    }

    fun loadAllNotification() : Flow<List<Notification>> {
        return notificationDao.loadAllNotifications()
    }

    fun loadNotificationByKeywords(query: String) : List<Notification> {
        return notificationDao.loadNotificationsWithKeywords(query)
    }

    fun loadUniqueCategories() : List<String?> {
        return notificationDao.loadUniqueCategories()
    }

    fun loadUniquePackages() : List<String> {
        return notificationDao.loadUniquePackages()
    }

    fun loadNotificationByCategories(
        categorySelectionList: List<String?>,
        containNull: Boolean
    ) : Flow<List<Notification>> {
        return notificationDao.loadNotificationsByCategories(
            categorySelectionList,
            containNull = containNull
        )
    }

    fun loadNotificationByApps(appSelectionList: List<String>): Flow<List<Notification>> {
        return notificationDao.loadNotificationByApps(appSelectionList)
    }

    fun loadNotificationByDate(date: Long?): Flow<List<Notification>> {
        if (date == null) {
            return emptyFlow()
        }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val nextDay = calendar.timeInMillis

        return notificationDao.loadNotificationByDate(date, nextDay)
    }

    fun loadNotificationByDateRange(firstDate: Long?, secondDate: Long?): Flow<List<Notification>> {
        if (firstDate == null) {
            return emptyFlow()
        }
        if (secondDate == null) {
            return loadNotificationByDate(firstDate)
        }

        // Since by default selected date starts at 00:00, when select a date range, user
        // often want notifications that included in the end dates -> Increment the second
        // date by one day to include all notifications in the second date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = secondDate
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val nextDay = calendar.timeInMillis
        return notificationDao.loadNotificationByDateRange(firstDate, nextDay)
    }

    fun countNotificationLast7Days() : List<NotificationCountByDate> {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val sevenDaysAgo = calendar.timeInMillis
        return notificationDao.countNotificationLast7Days(today, sevenDaysAgo)
    }

    fun countNotificationByAppLast7Days() : List<NotificationCountByApp> {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val sevenDaysAgo = calendar.timeInMillis
        return notificationDao.countNotificationByAppLast7Days(today, sevenDaysAgo)
    }
}