package dev.eliaschen.notificationlistener.room.table

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val notificationId: Int,
    val title: String,
    val text: String,
    val icon: Bitmap,
    val packageName: String,
    val timestamp: Long,
    val archived: Boolean = false
)