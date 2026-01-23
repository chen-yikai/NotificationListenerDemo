package dev.eliaschen.noti.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.eliaschen.noti.ui.components.AppBottomNavbar
import dev.eliaschen.noti.util.LocalNavStack
import dev.eliaschen.noti.util.LocalSnackBarHostState
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable
    data object Notification : Screen

    @Serializable
    data class Reminder(
        val method: ReminderMethod = ReminderMethod.None,
        val action: ReminderAction = ReminderAction.None
    ) : Screen
}

enum class ReminderMethod { None, Task, Memo }
enum class ReminderAction { None, NewTask, NewMemo }

@Composable
fun NavScreen() {
    val backStack = rememberNavBackStack(Screen.Notification)
    val snackbarHostState = remember { SnackbarHostState() }
    val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
        entry<Screen.Notification> { NotificationScreen() }
        entry<Screen.Reminder> { key -> ReminderScreen(key.method, key.action) }
    }

    CompositionLocalProvider(
        LocalSnackBarHostState provides snackbarHostState,
        LocalNavStack provides backStack
    ) {
        Surface {
            Scaffold(bottomBar = {
                AppBottomNavbar()
            }, snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { innerPadding ->
                NavDisplay(
                    backStack,
                    entryProvider = entryProvider,
                    onBack = { backStack.removeLastOrNull() })
            }
        }
    }
}
