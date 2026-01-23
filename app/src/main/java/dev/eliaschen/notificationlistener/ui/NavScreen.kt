package dev.eliaschen.notificationlistener.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.eliaschen.notificationlistener.ui.components.AppBottomNavbar
import dev.eliaschen.notificationlistener.util.LocalNavStack
import dev.eliaschen.notificationlistener.util.LocalSnackBarHostState
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Notification : NavKey

    @Serializable
    data object Reminder : NavKey
}

@Composable
fun NavScreen() {
    val backStack = rememberNavBackStack(Screen.Notification)
    val snackbarHostState = remember { SnackbarHostState() }
    val entryProvider = entryProvider {
        entry<Screen.Notification> { NotificationScreen() }
        entry<Screen.Reminder> { ReminderScreen() }
    }

    CompositionLocalProvider(
        LocalSnackBarHostState provides snackbarHostState,
        LocalNavStack provides backStack
    ) {
        Surface {
            Scaffold(bottomBar = {
                AppBottomNavbar()
            }, snackbarHost = { SnackbarHost(snackbarHostState) }
            ) {
                NavDisplay(
                    backStack,
                    entryProvider = entryProvider,
                    onBack = { backStack.removeLastOrNull() })
            }
        }
    }
}
