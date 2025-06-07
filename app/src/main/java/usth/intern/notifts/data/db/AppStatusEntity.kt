package usth.intern.notifts.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_status_entity")
data class AppStatusEntity(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val rowid: Long? = null,

    @ColumnInfo(name = "app_name")
    val appName: String,

    @ColumnInfo(name = "status")
    val status: Boolean
)
