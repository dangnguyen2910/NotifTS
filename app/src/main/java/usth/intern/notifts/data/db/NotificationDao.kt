package usth.intern.notifts.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

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
            "or category is null and :containNull = 1"
    )
    fun loadNotificationsByCategories(
        categorySelectionList: List<String?>,
        containNull: Boolean
    ) : Flow<List<Notification>>

    @Query("select rowid, * from notification where package_name in (:appSelectionList)")
    fun loadNotificationByApps(appSelectionList: List<String>): Flow<List<Notification>>

    // TODO: Fix me
//    @Query("select")
//    fun loadNotificationByDate(date: Long?): Flow<List<Notification>>
//
//    @Query("")
//    fun loadNotificationByDateRange(firstDate: Long?, secondDate: Long?): Flow<List<Notification>>
}