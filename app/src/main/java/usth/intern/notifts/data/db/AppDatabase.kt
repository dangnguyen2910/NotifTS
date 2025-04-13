package usth.intern.notifts.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notification::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun notificationDao(): NotificationDao
}