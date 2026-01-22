package dev.eliaschen.notificationlistener.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.eliaschen.notificationlistener.room.table.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Insert
    suspend fun addTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todoDao: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Query("UPDATE todo SET done = :done WHERE id = :id")
    suspend fun updateTodoStatus(id: Long, done: Boolean)
}