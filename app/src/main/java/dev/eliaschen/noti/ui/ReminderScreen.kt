package dev.eliaschen.noti.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.eliaschen.noti.ui.components.SingleChooseTabRow
import dev.eliaschen.noti.ui.components.TaskBottomSheet
import dev.eliaschen.noti.ui.components.TaskTabContent
import dev.eliaschen.noti.utils.ReminderTab
import dev.eliaschen.noti.viewmodel.TaskViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    method: ReminderMethod,
    action: ReminderAction,
    viewModel: TaskViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(ReminderTab.Task) }
    val allTodo by viewModel.allTodo.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(method) {
        delay(100)
        when (method) {
            ReminderMethod.Task -> selectedTab = ReminderTab.Task
            ReminderMethod.Memo -> selectedTab = ReminderTab.Memo
            else -> {}
        }
    }

    LaunchedEffect(action) {
        when (action) {
            ReminderAction.NewTask -> showBottomSheet = true
            ReminderAction.NewMemo -> {}
            else -> {}
        }
    }
    if (showBottomSheet) {
        TaskBottomSheet(sheetState = sheetState) {
            showBottomSheet = false
        }
    }
    Scaffold(topBar = {
        SingleChooseTabRow(
            selectedTab,
            modifier = Modifier
                .statusBarsPadding()
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            selectedTab = it
        }
    }) { innerPadding ->
        val todoTasks = allTodo.filter { !it.done }
        val doneTasks = allTodo.filter { it.done }

        TaskTabContent(
            todoTasks = todoTasks,
            doneTasks = doneTasks,
            onTaskCheckedChange = { taskId, checked ->
                viewModel.updateTaskStatus(taskId, checked)
            },
            innerPadding = innerPadding
        )
    }
}