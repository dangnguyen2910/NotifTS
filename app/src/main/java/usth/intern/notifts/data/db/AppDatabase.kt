package usth.intern.notifts.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notification::class, AppStatusEntity::class], version = 6)
abstract class AppDatabase : RoomDatabase(){
    abstract fun notificationDao(): NotificationDao
    abstract fun appStatusDao(): AppStatusDao
}