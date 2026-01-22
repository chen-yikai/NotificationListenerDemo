package dev.eliaschen.notificationlistener.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.eliaschen.notificationlistener.ui.components.AppBottomNavbar
import kotlinx.serialization.Serializable

@Serializable
data object NotificationScreen

@Serializable
data object ReminderScreen

@Composable
fun NavScreen() {
    val backStack = remember { mutableStateListOf<Any>(NotificationScreen) }
    val entryProvider = entryProvider {
        entry<NotificationScreen> { NotificationList() }
        entry<ReminderScreen> { }
    }

    Surface {
        Scaffold(bottomBar = {
            AppBottomNavbar()
        }) {
            NavDisplay(
                backStack,
                entryProvider = entryProvider,
                onBack = { backStack.removeLastOrNull() })
        }
    }
}
