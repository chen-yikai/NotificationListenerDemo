package dev.eliaschen.noti.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.eliaschen.noti.room.table.TaskEntity

@Composable
fun TaskTabContent(
    todoTasks: List<TaskEntity>,
    doneTasks: List<TaskEntity>,
    onTaskCheckedChange: (Long, Boolean) -> Unit,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(PaddingValues(top = innerPadding.calculateTopPadding()))
            .fillMaxHeight()
    ) {
        // Todo section
        TaskListSection(
            title = "Todo",
            tasks = todoTasks,
            emptyMessage = "No tasks yet\nCreate one to get started!",
            onTaskCheckedChange = onTaskCheckedChange,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        
        // Done section
        TaskListSection(
            title = "Done",
            tasks = doneTasks,
            emptyMessage = "No completed tasks yet",
            onTaskCheckedChange = onTaskCheckedChange,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            showCollapsibleHeader = true,
            onHeaderClick = { /* TODO: Implement collapse/expand */ },
            showBottomSpacer = true
        )
    }
}
