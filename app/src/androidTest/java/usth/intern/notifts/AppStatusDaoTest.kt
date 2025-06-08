package usth.intern.notifts

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import usth.intern.notifts.data.db.AppDatabase
import usth.intern.notifts.data.db.AppStatusDao
import usth.intern.notifts.data.db.AppStatusEntity
import java.io.IOException

class AppStatusDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var appStatusDao: AppStatusDao
    private val appStatus1: AppStatusEntity = AppStatusEntity(appName = "Messenger", status = true)
    private val appStatus2: AppStatusEntity = AppStatusEntity(appName = "Facebook", status = true)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        appStatusDao = db.appStatusDao()
        runBlocking {
            launch {
                appStatusDao.insertAppStatus(appStatus1)
                appStatusDao.insertAppStatus(appStatus2)
            }
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun updateAppStatusTest() {
        val newAppStatus1 = AppStatusEntity(1, "Messenger", false)
        val appStatus2Copy = appStatus2.copy(rowid = 2)
        val expect = listOf(appStatus2Copy, newAppStatus1)

        runBlocking {
            launch {
                appStatusDao.updateAppStatus(newAppStatus1)
                val appStatusList = appStatusDao.getAllAppStatus()
                assertEquals(expect, appStatusList)
            }
        }
    }

    @Test
    fun getStatusListTest() {
        val expect = listOf(true, true)
        runBlocking {
            launch {
                val statusList = appStatusDao.getStatusList()
                assertEquals(expect, statusList)
            }
        }
    }


    @Test
    fun getInactiveAppTest() {
        val newAppStatus1 = AppStatusEntity(1, "Messenger", false)
        val expect = listOf(newAppStatus1)

        runBlocking {
            launch {
                appStatusDao.updateAppStatus(newAppStatus1)
                val inactiveApp = appStatusDao.getInactiveApp()
                assertEquals(expect, inactiveApp)
            }
        }
    }
}