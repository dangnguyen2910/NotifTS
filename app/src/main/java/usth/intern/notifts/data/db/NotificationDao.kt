package usth.intern.notifts.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificationDao {
    @Insert
    suspend fun insertNotification(notification: Notification)

    @Query(
        "select * " +
        "from notification " +
        "order by rowid desc " +
        "limit :num"
    )
    suspend fun loadNewestNotifications(num: Int = 1): Notification
}