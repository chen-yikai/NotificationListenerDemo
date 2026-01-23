package dev.eliaschen.noti.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.eliaschen.noti.room.dao.TodoDao
import javax.inject.Inject

@HiltViewModel
class PromptViewModel @Inject constructor(private val todo: TodoDao) : ViewModel() {

}