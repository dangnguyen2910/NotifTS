package usth.intern.notifts.data

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import usth.intern.notifts.data.db.AppDatabase
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
}