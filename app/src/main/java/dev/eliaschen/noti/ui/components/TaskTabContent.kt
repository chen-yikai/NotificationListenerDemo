package dev.eliaschen.noti.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.eliaschen.noti.room.table.TaskEntity
import dev.eliaschen.noti.utils.LocalRootScaffoldPadding

@Composable
fun TaskTabContent(
    todoTasks: List<TaskEntity>,
    doneTasks: List<TaskEntity>,
    onTaskCheckedChange: (Long, Boolean) -> Unit,
    innerPadding: PaddingValues
) {
    val scaffoldPadding = LocalRootScaffoldPadding.current
    var todoExpanded by remember { mutableStateOf(true) }
    var doneExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .padding(
                top = innerPadding.calculateTopPadding(),
                bottom = scaffoldPadding.calculateBottomPadding()
            )
            .fillMaxSize()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        TaskListSection(
            title = "Todo",
            tasks = todoTasks,
            emptyMessage = "No tasks yet\nCreate one to get started!",
            onTaskCheckedChange = onTaskCheckedChange,
            isExpanded = todoExpanded,
            onExpandChange = { todoExpanded = it }
        )
        TaskListSection(
            title = "Done",
            tasks = doneTasks,
            emptyMessage = "No completed tasks yet",
            onTaskCheckedChange = onTaskCheckedChange,
            isExpanded = doneExpanded,
            onExpandChange = { doneExpanded = it },
            showBottomSpacer = true
        )
    }
}
