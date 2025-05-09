package usth.intern.notifts

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
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
    private lateinit var notification3: Notification

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

        notification3 = Notification(
            packageName = "com.google.whatever",
            title = "Another new email",
            text = "This is a new email from whatever",
            bigText = "More content of the new email",
            category = "mail",
            date = "Tomorrow"
        )

        runBlocking {
            launch {
                notificationDao.insertNotification(notification1)
                notificationDao.insertNotification(notification2)
                notificationDao.insertNotification(notification3)
            }
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndLoadNewestNotificationTest() {
        val expected = notification3.copy(rowid = 3)

        runBlocking {
            launch {
                val newestNotification = notificationDao.loadNewestNotification().first()
                assertEquals(newestNotification, expected)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    @Ignore("Deprecated")
    fun insertMultipleAndLoadNewestNotificationTest() {
        val expected = notification2.copy(rowid = 2)

        runBlocking {
            launch {
                val newestNotification = notificationDao.loadNewestNotification().first()
                assertEquals(newestNotification, expected)
            }
        }
    }

    @Test
    fun insertMultipleAndLoadAllNotifications() {
        val expect1 = notification1.copy(rowid = 1)
        val expect2 = notification2.copy(rowid = 2)
        val expect3 = notification3.copy(rowid = 3)
        val expectList = listOf(expect3, expect2, expect1)
        runBlocking {
            launch {
                val allNotification = notificationDao.loadAllNotifications().first()
                assertEquals(allNotification, expectList)
            }
        }
    }

    @Test
    fun queryNotificationGivenKeywords_Return1Notification() {
        val expect1 = notification1.copy(rowid = 1)
        val expectList = listOf(expect1)

        runBlocking {
            launch {
                val notificationList = notificationDao.loadNotificationsWithKeywords("title this")
                assertEquals(notificationList, expectList)
            }
        }
    }

    @Test
    fun queryNotificationGivenKeywords_Return2Notification() {
        val expect1 = notification1.copy(rowid = 1)
        val expect2 = notification2.copy(rowid = 2)
        val expect3 = notification3.copy(rowid = 3)
        val expectList = listOf(expect3, expect2, expect1)

        runBlocking {
            launch {
                val notificationList = notificationDao.loadNotificationsWithKeywords("content ")
                assertEquals(expectList, notificationList)
            }
        }
    }

    @Test
    fun loadUniqueCategory_Return2Strings() {
        val expectList = listOf(null, "mail")
        runBlocking {
            launch {
                val categoryList = notificationDao.loadUniqueCategories()
                assertEquals(expectList, categoryList)
            }
        }
    }

    @Test
    fun loadUniquePackages_Return2Strings() {
        val expectList = listOf("com.google.gm", "com.google.whatever")
        runBlocking {
            launch {
                val packageList = notificationDao.loadUniquePackages()
                assertEquals(expectList, packageList)
            }
        }
    }

    @Test
    fun loadNotificationsByCategories_ContainNull() {
        val expect1 = notification1.copy(rowid = 1)
        val expect2 = notification2.copy(rowid = 2)
        val expect3 = notification3.copy(rowid = 3)
        val expectList = listOf(expect1, expect2, expect3)

        runBlocking {
            launch {
                val categoryList = listOf(null, "mail")
                val notificationList = notificationDao
                    .loadNotificationsByCategories(categoryList, true)
                    .first()
                assertEquals(expectList, notificationList)
            }
        }
    }

    @Test
    fun loadNotificationsByCategories_NoNull() {
        val expect2 = notification2.copy(rowid = 2)
        val expect3 = notification3.copy(rowid = 3)
        val expectList = listOf(expect2, expect3)

        runBlocking {
            launch {
                val categoryList = listOf(null, "mail")
                val notificationList = notificationDao
                    .loadNotificationsByCategories(categoryList, false)
                    .first()
                assertEquals(expectList, notificationList)
            }
        }
    }

    @Test
    fun loadNotificationsByApps() {
        val expect2 = notification2.copy(rowid = 2)
        val expect3 = notification3.copy(rowid = 3)
        val expectList = listOf(expect2, expect3)

        runBlocking {
            launch {
                val appList = listOf("com.google.whatever")
                val notificationList = notificationDao
                    .loadNotificationByApps(appList)
                    .first()
                assertEquals(expectList, notificationList)
            }
        }
    }
}