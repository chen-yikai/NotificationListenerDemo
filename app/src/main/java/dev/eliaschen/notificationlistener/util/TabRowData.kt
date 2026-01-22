package dev.eliaschen.notificationlistener.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.StickyNote2
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector

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
    val spotlight: Boolean = false
) : TabRowData {
    Task("Notification", Icons.Rounded.Notifications),
//    Action("Action", Icons.Rounded.Add, true),
    Memo("Reminder", Icons.Rounded.TaskAlt)
}