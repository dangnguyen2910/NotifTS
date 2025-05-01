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
        "select * " +
        "from notification " +
        "order by rowid desc " +
        "limit 1"
    )
    fun loadNewestNotification(): Flow<Notification?>

    @Query(
        "select * " +
        "from notification"
    )
    fun loadAllNotifications(): Flow<List<Notification>>
}