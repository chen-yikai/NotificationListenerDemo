package dev.eliaschen.notificationlistener.room.table

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Creator {
    USER, AUTO
}

@Entity(tableName = "todo")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val done: Boolean = false,
    val creator: Creator = Creator.USER,
    val dueDate: Long? = null,
    val timestamp: Long
)