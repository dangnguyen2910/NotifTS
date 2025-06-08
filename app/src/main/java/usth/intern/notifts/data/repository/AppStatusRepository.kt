package usth.intern.notifts.data.repository

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import usth.intern.notifts.data.db.AppDatabase
import usth.intern.notifts.data.db.AppStatusEntity
import usth.intern.notifts.domain.AppStatus
import javax.inject.Inject

class AppStatusRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val db = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "notification-database"
    ).fallbackToDestructiveMigration(dropAllTables = true).build()

    private val appStatusDao = db.appStatusDao()

    private fun AppStatus.toEntity() : AppStatusEntity {
        return AppStatusEntity(
            rowid = rowid,
            appName = appName,
            status = status,
        )
    }

    private fun AppStatusEntity.toDomainObject() : AppStatus {
        return AppStatus(rowid, appName, status)
    }

    fun insertAppStatus(appStatus: AppStatus) {
        val appStatusEntity = appStatus.toEntity()
        appStatusDao.insertAppStatus(appStatusEntity)
    }

    fun updateAppStatus(appStatus: AppStatus) {
        val appStatusEntity = appStatus.toEntity()
        appStatusDao.updateAppStatus(appStatusEntity)
    }

    fun getAllAppStatus() : List<AppStatus> {
        val appStatusEntityList = appStatusDao.getAllAppStatus()
        val appStatusList = appStatusEntityList.map { appStatusEntity ->
            appStatusEntity.toDomainObject()
        }
        return appStatusList
    }

    fun getStatusList() : List<Boolean> {
        return appStatusDao.getStatusList()
    }

    fun getInactiveApp() : List<AppStatus> {
        val inactiveAppEntityList = appStatusDao.getInactiveApp()
        val inactiveAppList = inactiveAppEntityList.map { appStatusEntity ->
            appStatusEntity.toDomainObject()
        }
        return inactiveAppList
    }
}