package dev.eliaschen.notificationlistener.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.eliaschen.notificationlistener.ui.NotificationTab

@Composable
fun EmptyNotificationState(selectedTab: NotificationTab) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            if (selectedTab == NotificationTab.Active) "No active notifications yet" else "No archived notifications yet",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
