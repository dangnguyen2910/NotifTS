package usth.intern.notifts.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4
@Entity(tableName = "notification")
data class Notification(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val rowid: Long? = null,

    @ColumnInfo(name = "package_name")
    val packageName: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "big_text")
    val bigText: String?,

    @ColumnInfo(name = "category")
    val category: String?,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
