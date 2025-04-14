package usth.intern.notifts.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

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
    suspend fun loadNewestNotification(): Notification
}