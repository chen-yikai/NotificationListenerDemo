package dev.eliaschen.noti.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.eliaschen.noti.room.table.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAllTodos(): Flow<List<TaskEntity>>

    @Insert
    suspend fun addTodo(todo: TaskEntity)

    @Delete
    suspend fun deleteTodo(todoDao: TaskEntity)

    @Update
    suspend fun updateTodo(todo: TaskEntity)

    @Query("UPDATE task SET done = :done WHERE id = :id")
    suspend fun updateTodoStatus(id: Long, done: Boolean)
}