package dev.eliaschen.noti.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.StickyNote2
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import dev.eliaschen.noti.ui.Screen
import org.checkerframework.checker.guieffect.qual.UI

interface TabRowData {
    val label: String
    val icon: ImageVector
}

enum class NotificationTab(override val label: String, override val icon: ImageVector) :
    TabRowData {
    Active("Active", Icons.Rounded.Notifications),
    History("History", Icons.Rounded.History)
}

enum class ReminderTab(override val label: String, override val icon: ImageVector) : TabRowData {
    Task("Task", Icons.Rounded.TaskAlt),
    Memo("Memo", Icons.Rounded.StickyNote2)
}

enum class BottomNav(
    override val label: String,
    override val icon: ImageVector,
    val route: NavKey
) : TabRowData {
    Task("Notification", Icons.Rounded.Notifications, Screen.Notification),
    Memo("Reminder", Icons.Rounded.TaskAlt, Screen.Reminder())
}

enum class SpotlightAction(
    override val label: String,
    override val icon: ImageVector,
) : TabRowData {
    Task("Task", Icons.Rounded.TaskAlt),
    Memo("Memo", Icons.Rounded.StickyNote2)
}

data class ExtraOption(
    val label: String,
    val icon: ImageVector,
    val active: Boolean = false,
    val timestamp: Long = 0L,
    val format: String = "yyyy/MM/dd",
    val extraAction: (() -> Unit)? = null,
    val action: () -> Unit
)
