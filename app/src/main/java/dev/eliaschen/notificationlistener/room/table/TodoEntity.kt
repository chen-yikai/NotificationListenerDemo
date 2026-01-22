package dev.eliaschen.notificationlistener.room.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val done: Boolean = false,
    val timestamp: Long
)