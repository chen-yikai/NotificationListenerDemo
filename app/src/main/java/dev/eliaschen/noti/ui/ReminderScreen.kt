package dev.eliaschen.noti.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.eliaschen.noti.ui.components.SingleChooseTabRow
import dev.eliaschen.noti.util.ReminderTab
import dev.eliaschen.noti.viewmodel.TodoViewModel

@Composable
fun ReminderScreen(viewModel: TodoViewModel = hiltViewModel()) {
    var selectedTab by remember { mutableStateOf(ReminderTab.Task) }
    val allTodo by viewModel.allTodo.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

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
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                start = 20.dp,
                end = 20.dp
            )
        ) {
            items(allTodo, key = { it.id }) {
                Card {
                    Checkbox(it.done, onCheckedChange = {})
                    Text(it.title)
                }
            }
        }
    }
}