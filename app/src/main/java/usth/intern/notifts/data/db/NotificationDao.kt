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
}