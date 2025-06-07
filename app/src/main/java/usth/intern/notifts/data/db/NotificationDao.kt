package usth.intern.notifts.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import usth.intern.notifts.domain.NotificationCountByApp
import usth.intern.notifts.domain.NotificationCountByDate

@Dao
interface NotificationDao {
    @Insert
    suspend fun insertNotification(notification: Notification): Long

    @Query(
        "select rowid, *  " +
        "from notification " +
        "order by rowid desc " +
        "limit 1"
    )
    fun loadNewestNotification(): Flow<Notification?>

    @Query(
        "select rowid, * " +
        "from notification " +
        "order by rowid desc"
    )
    fun loadAllNotifications(): Flow<List<Notification>>

    @Query(
        "select rowid, * " +
        "from notification " +
        "where notification match :query " +
        "order by rowid desc"
    )
    fun loadNotificationsWithKeywords(query: String): List<Notification>

    @Query("select distinct category from notification order by category")
    fun loadUniqueCategories(): List<String?>

    @Query("select distinct package_name from notification order by package_name")
    fun loadUniquePackages(): List<String>

    @Query(
        "select rowid, * " +
        "from notification " +
        "where category in (:categorySelectionList)" +
            "or category is null and :containNull = 1 " +
        "order by rowid desc"
    )
    fun loadNotificationsByCategories(
        categorySelectionList: List<String?>,
        containNull: Boolean
    ) : Flow<List<Notification>>

    @Query(
        "select rowid, * " +
        "from notification " +
        "where package_name in (:appSelectionList) " +
        "order by rowid desc"
    )
    fun loadNotificationByApps(appSelectionList: List<String>): Flow<List<Notification>>

    @Query(
        "select rowid, * " +
        "from notification " +
        "where timestamp >= :selectedDate AND timestamp < :nextDay " +
        "order by rowid desc"
    )
    fun loadNotificationByDate(selectedDate: Long?, nextDay: Long?): Flow<List<Notification>>

    @Query(
        "select rowid, * " +
        "from notification " +
        "where timestamp BETWEEN :firstDate AND :secondDate " +
        "order by rowid desc"
    )
    fun loadNotificationByDateRange(firstDate: Long?, secondDate: Long?): Flow<List<Notification>>

    @Query(
        "select " +
            "strftime('%d-%m-%Y', timestamp / 1000, 'unixepoch') as notificationDate, " +
            "count(*) as notificationCount " +
        "from notification " +
        "where timestamp " +
        "BETWEEN :sevenDaysAgo AND :today " +
        "GROUP BY notificationDate order by rowid desc"
    )
    fun countNotificationLast7Days(today: Long, sevenDaysAgo: Long): List<NotificationCountByDate>

    @Query("select package_name as app, count(*) as notificationCount " +
            "from notification " +
            "where timestamp BETWEEN :sevenDaysAgo AND :today " +
            "group by package_name " +
            "order by notificationCount desc " +
            "limit 5"
    )
    fun countNotificationByAppLast7Days(today: Long, sevenDaysAgo: Long): List<NotificationCountByApp>
}