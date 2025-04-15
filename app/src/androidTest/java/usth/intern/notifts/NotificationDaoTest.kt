package usth.intern.notifts

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import usth.intern.notifts.data.db.AppDatabase
import usth.intern.notifts.data.db.Notification
import usth.intern.notifts.data.db.NotificationDao
import java.io.IOException

class NotificationDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var notificationDao: NotificationDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        notificationDao = db.notificationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndLoadNewestNotificationTest() {
        val notification = Notification(
            packageName = "test",
            title = "This is a title",
            text = "This is the content of notification",
            bigText = null,
            category = null,
            date = "Today"
        )

        val expected = notification.copy(rowid = 1)

        runBlocking {
            launch {
                notificationDao.insertNotification(notification)
                val newestNotification = notificationDao.loadNewestNotification().first()
                assertEquals(newestNotification, expected)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertMultipleAndLoadNewestNotificationTest() {
        val notification1 = Notification(
            packageName = "com.google.gm",
            title = "This is a title",
            text = "This is the content of notification",
            bigText = null,
            category = null,
            date = "Today"
        )

        val notification2 = Notification(
            packageName = "com.google.whatever",
            title = "New email",
            text = "This is a new email",
            bigText = "More content of the new email",
            category = "mail",
            date = "Tomorrow"
        )

        val expected = notification2.copy(rowid = 2)

        runBlocking {
            launch {
                notificationDao.insertNotification(notification1)
                notificationDao.insertNotification(notification2)
                val newestNotification = notificationDao.loadNewestNotification().first()
                assertEquals(newestNotification, expected)
            }
        }
    }

    @Test
    fun loadNewestNotificationWithEmptyDatabase() {
        runBlocking {
            launch {
                val newestNotification = notificationDao.loadNewestNotification().first()
                assertNull(newestNotification)
            }
        }
    }

}