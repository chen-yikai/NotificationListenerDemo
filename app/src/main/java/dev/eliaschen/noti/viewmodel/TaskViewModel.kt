package dev.eliaschen.noti.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.eliaschen.noti.room.dao.TaskDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {
    val allTodo = taskDao.getAllTodos().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}