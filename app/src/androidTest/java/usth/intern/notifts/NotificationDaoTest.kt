package usth.intern.notifts

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import usth.intern.notifts.data.db.AppDatabase
import usth.intern.notifts.data.db.Notification
import usth.intern.notifts.data.db.NotificationDao
import java.io.IOException

class NotificationDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var notificationDao: NotificationDao
    private lateinit var notification1: Notification
    private lateinit var notification2: Notification

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        notificationDao = db.notificationDao()

        notification1 = Notification(
            packageName = "com.google.gm",
            title = "This is a title",
            text = "This is the content of notification",
            bigText = null,
            category = null,
            date = "Today"
        )

        notification2 = Notification(
            packageName = "com.google.whatever",
            title = "New email",
            text = "This is a new email",
            bigText = "More content of the new email",
            category = "mail",
            date = "Tomorrow"
        )
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

    @Test
//    @Ignore("Skip this for now")
    fun insertMultipleAndLoadAllNotifications() {
        val expect1 = notification1.copy(rowid = 1)
        val expect2 = notification2.copy(rowid = 2)
        val expectList = listOf(expect1, expect2)
        runBlocking {
            launch {
                notificationDao.insertNotification(notification1)
                notificationDao.insertNotification(notification2)
                val allNotification = notificationDao.loadAllNotifications().first()
                assertEquals(allNotification, expectList)
            }
        }
    }

}