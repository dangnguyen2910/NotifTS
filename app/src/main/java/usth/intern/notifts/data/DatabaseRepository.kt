package usth.intern.notifts.data

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import usth.intern.notifts.data.db.AppDatabase
import usth.intern.notifts.data.db.Notification
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
    val newestNotification = notificationDao.loadNewestNotification()

    suspend fun insertNotification(
        packageName: String,
        title: String,
        text: String,
        bigText: String?,
        category: String?,
        date: String
    ) : Long{
        val notification = Notification(
            packageName = packageName,
            title = title,
            text = text,
            bigText = bigText,
            category = category,
            date = date
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
        return notificationDao.loadNotificationByDate(date)
    }

    fun loadNotificationByDateRange(firstDate: Long?, secondDate: Long?): Flow<List<Notification>> {
        return notificationDao.loadNotificationByDateRange(firstDate, secondDate)
    }

}