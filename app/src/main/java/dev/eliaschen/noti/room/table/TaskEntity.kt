package dev.eliaschen.noti.room.table

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Creator {
    USER, AUTO
}

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val date: Long? = null,  // Date in epoch millis (00:00:00 of the day)
    val time: Long? = null,  // Time in minutes from midnight (0-1439), null means no specific time
    val detail: String?,
    val done: Boolean = false,
    val creator: Creator = Creator.USER,
    val createdAt: Long, val updatedAt: Long
)
