package dev.eliaschen.noti.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.eliaschen.noti.ui.components.SingleChooseTabRow
import dev.eliaschen.noti.ui.components.TaskBottomSheet
import dev.eliaschen.noti.ui.components.TaskCard
import dev.eliaschen.noti.utils.LocalNavStack
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

        Column(
            modifier = Modifier
                .padding(PaddingValues(top = innerPadding.calculateTopPadding()))
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 50.dp)
                ) {
                    items(
                        todoTasks,
                        key = { it.id },
                        contentType = { "task" }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onCheckedChange = { checked ->
                                viewModel.updateTaskStatus(task.id, checked)
                            },
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .animateItem(
                                    fadeInSpec = spring(stiffness = Spring.StiffnessLow),
                                    fadeOutSpec = spring(stiffness = Spring.StiffnessLow),
                                    placementSpec = spring(
                                        stiffness = Spring.StiffnessLow,
                                        dampingRatio = Spring.DampingRatioMediumBouncy
                                    )
                                )
                        )
                    }
                }
                if (todoTasks.isEmpty()) {
                    Text(
                        text = "No tasks yet\nCreate one to get started!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.Center)
                    )
                }
                Text(
                    text = "Todo",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.background, Color.Transparent
                                )
                            )
                        )
                        .padding(top = 10.dp)
                        .padding(horizontal = 15.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 50.dp)
                ) {
                    items(
                        doneTasks,
                        key = { it.id },
                        contentType = { "task" }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onCheckedChange = { checked ->
                                viewModel.updateTaskStatus(task.id, checked)
                            },
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .animateItem(
                                    fadeInSpec = spring(stiffness = Spring.StiffnessLow),
                                    fadeOutSpec = spring(stiffness = Spring.StiffnessLow),
                                    placementSpec = spring(
                                        stiffness = Spring.StiffnessLow,
                                        dampingRatio = Spring.DampingRatioMediumBouncy
                                    )
                                )
                        )
                    }
                    item {
                        Spacer(
                            Modifier
                                .navigationBarsPadding()
                                .padding(30.dp)
                        )
                    }
                }
                if (doneTasks.isEmpty()) {
                    Text(
                        text = "No completed tasks yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.Center)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .clickable {
                        }
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null)
                }
                HorizontalDivider(modifier = Modifier.align(Alignment.TopCenter))
            }

        }
    }
}