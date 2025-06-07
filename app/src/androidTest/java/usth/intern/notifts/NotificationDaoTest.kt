package usth.intern.notifts

import android.content.Context
import android.util.Log
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
import usth.intern.notifts.domain.NotificationCountByApp
import usth.intern.notifts.domain.NotificationCountByDate
import usth.intern.notifts.data.db.NotificationDao
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

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

        val date1 = "01-01-2025 03:00:00"
        val date2 = "03-01-2025 12:00:00"
        val date3 = "04-01-2025 15:00:00"

        val dateLong1 = dateStringToLong(date1)
        val dateLong2 = dateStringToLong(date2)
        val dateLong3 = dateStringToLong(date3)

        Log.d("NotificationDaoTest", "Date Long1: $dateLong1")
        Log.d("NotificationDaoTest", "Date Long2: $dateLong2")
        Log.d("NotificationDaoTest", "Date Long3: $dateLong3")

        notification1 = Notification(
            packageName = "com.google.gm",
            title = "This is a title",
            text = "This is the content of notification",
            bigText = null,
            category = null,
            timestamp = dateLong1,
        )

        notification2 = Notification(
            packageName = "com.google.whatever",
            title = "New email",
            text = "This is a new email",
            bigText = "More content of the new email",
            category = "mail",
            timestamp = dateLong2,
        )

        notification3 = Notification(
            packageName = "com.google.whatever",
            title = "Another new email",
            text = "This is a new email from whatever",
            bigText = "More content of the new email",
            category = "mail",
            timestamp = dateLong3
        )

        runBlocking {
            launch {
                notificationDao.insertNotification(notification1)
                notificationDao.insertNotification(notification2)
                notificationDao.insertNotification(notification3)
            }
        }
    }

    private fun dateStringToLong(dateString: String) : Long {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val date = inputFormat.parse(dateString)
            val dateLong = date!!.time
            return dateLong
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
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
        val expectList = listOf(expect3, expect2, expect1)

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
        val expectList = listOf(expect3, expect2)

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
        val expectList = listOf(expect3, expect2)

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

    @Test
    fun loadNotificationByDate() {
        val dateString = "01-01-2025 00:00:00"
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val dateLong = formatter.parse(dateString)!!.time

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateLong
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val nextDay = calendar.timeInMillis
        Log.d("NotificationDaoTest", "Today: $dateLong")
        Log.d("NotificationDaoTest", "Next day: $nextDay")

        val expect1 = notification1.copy(rowid=1)
        val expectList = listOf(expect1)

        runBlocking {
            launch {
                val notificationList = notificationDao
                    .loadNotificationByDate(dateLong, nextDay)
                    .first()
                assertEquals(expectList, notificationList)
            }
        }
    }

    @Test
    fun loadNotificationByDateRange() {
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        val dateString = "01-01-2025 00:00:00"
        val dateLong = formatter.parse(dateString)!!.time

        val nextDay = formatter.parse("05-01-2025 00:00:00")!!.time

        Log.d("NotificationDaoTest", "Today: $dateLong")
        Log.d("NotificationDaoTest", "Next day: $nextDay")

        val expect1 = notification1.copy(rowid=1)
        val expect2 = notification2.copy(rowid=2)
        val expect3 = notification3.copy(rowid=3)
        val expectList = listOf(expect3, expect2, expect1)

        runBlocking {
            launch {
                val notificationList = notificationDao
                    .loadNotificationByDate(dateLong, nextDay)
                    .first()
                assertEquals(expectList, notificationList)
            }
        }
    }

    @Test
    fun countNotificationLast7Days() {
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val calendar = Calendar.getInstance()

        val dateString = "05-01-2025 00:00:00"
        val today = formatter.parse(dateString)!!.time
        Log.d("NotificationDaoTest", "Today: $today")

        calendar.timeInMillis = today

        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val sevenDaysAgo = calendar.timeInMillis
        Log.d("NotificationDaoTest", "Seven days ago: $sevenDaysAgo")

        val expectation = listOf(
            NotificationCountByDate("04-01-2025", 1),
            NotificationCountByDate("03-01-2025", 1),
            NotificationCountByDate("01-01-2025", 1),
        )

        runBlocking {
            launch {
                val notificationCount = notificationDao
                    .countNotificationLast7Days(today, sevenDaysAgo)
                assertEquals(expectation, notificationCount)
            }
        }
    }

    @Test
    fun countNotificationByAppLast7Days() {
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val calendar = Calendar.getInstance()

        val dateString = "05-01-2025 00:00:00"
        val today = formatter.parse(dateString)!!.time
        Log.d("NotificationDaoTest", "Today: $today")

        calendar.timeInMillis = today

        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val sevenDaysAgo = calendar.timeInMillis
        Log.d("NotificationDaoTest", "Seven days ago: $sevenDaysAgo")

        val expectation = listOf(
            NotificationCountByApp("com.google.whatever", 2),
            NotificationCountByApp("com.google.gm", 1),
        )

        runBlocking {
            launch {
                val notificationCount = notificationDao
                    .countNotificationByAppLast7Days(today, sevenDaysAgo)
                assertEquals(expectation, notificationCount)
            }
        }
    }
}