package dev.eliaschen.noti.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.StickyNote2
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import dev.eliaschen.noti.ui.Screen

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
    Memo("Reminder", Icons.Rounded.TaskAlt, Screen.Reminder)
}

enum class SpotlightAction(
    override val label: String,
    override val icon: ImageVector,
    action: () -> Unit
) : TabRowData {
    Task("Task", Icons.Rounded.TaskAlt, {}),
    Memo("Memo", Icons.Rounded.StickyNote2, {})
}