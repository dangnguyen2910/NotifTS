package usth.intern.notifts.data

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
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
    ).build()

    private val notificationDao = db.notificationDao()

    suspend fun insertNotification(
        packageName: String,
        title: String,
        text: String?,
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
}