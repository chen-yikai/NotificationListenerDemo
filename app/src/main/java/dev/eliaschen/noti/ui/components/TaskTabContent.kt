package dev.eliaschen.noti.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.eliaschen.noti.utils.LocalRootScaffoldPadding
import dev.eliaschen.noti.viewmodel.TaskViewModel

@Composable
fun TaskTabContent(
    viewModel: TaskViewModel = hiltViewModel(),
    innerPadding: PaddingValues
) {
    val scaffoldPadding = LocalRootScaffoldPadding.current
    var todoExpanded by rememberSaveable { mutableStateOf(true) }
    var doneExpanded by rememberSaveable { mutableStateOf(false) }
    val tasks by viewModel.allTodo.collectAsStateWithLifecycle()
    val doneTasks = tasks.filter { it.done }
    val todoTasks = tasks.filter { !it.done }

    fun onTaskCheckedChange(id: Long, checked: Boolean) {
        viewModel.updateTaskStatus(id, checked)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .padding(
                top = innerPadding.calculateTopPadding(),
                bottom = scaffoldPadding.calculateBottomPadding()
            )
            .fillMaxSize()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        TaskListSection(
            title = "Task",
            tasks = todoTasks,
            emptyMessage = "No tasks yet, Create one!",
            onTaskCheckedChange = { id, checked ->
                onTaskCheckedChange(id, checked)
            },
            isExpanded = todoExpanded,
            onExpandChange = { todoExpanded = it }
        )
        TaskListSection(
            title = "Completed",
            tasks = doneTasks,
            emptyMessage = "No completed tasks yet",
            onTaskCheckedChange = { id, checked ->
                onTaskCheckedChange(id, checked)
            },
            isExpanded = doneExpanded,
            onExpandChange = { doneExpanded = it },
        )
    }
}
