package dev.eliaschen.notificationlistener.room

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val text: String,
    val icon: Bitmap,
    val packageName: String,
    val timestamp: Long
)