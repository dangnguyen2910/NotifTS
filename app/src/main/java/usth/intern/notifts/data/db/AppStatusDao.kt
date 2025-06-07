package usth.intern.notifts.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppStatusDao {
    @Insert
    fun insertAppStatus(appStatus: AppStatusEntity)

    @Update
    fun updateAppStatus(appStatus:AppStatusEntity)

    @Query("select rowid, * from app_status_entity order by app_name")
    fun getAllAppStatus(): List<AppStatusEntity>
}