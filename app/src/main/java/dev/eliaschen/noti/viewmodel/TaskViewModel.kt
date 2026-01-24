package dev.eliaschen.noti.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.eliaschen.noti.room.dao.TaskDao
import dev.eliaschen.noti.room.table.TaskEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {
    val allTodo = taskDao.getAllTodos().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.addTodo(task)
        }
    }
    
    fun updateTaskStatus(id: Long, done: Boolean) {
        viewModelScope.launch {
            taskDao.updateTodoStatus(id, done)
        }
    }
    
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.deleteTodo(task)
        }
    }
}
