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
    val time: Long? = null,
    val detail: String?,
    val done: Boolean = false,
    val creator: Creator = Creator.USER,
    val createdAt: Long, val updatedAt: Long
)