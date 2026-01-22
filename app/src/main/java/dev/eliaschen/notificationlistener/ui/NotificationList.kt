package dev.eliaschen.notificationlistener.ui

import android.app.ActivityOptions
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.eliaschen.notificationlistener.ui.components.EmptyNotificationState
import dev.eliaschen.notificationlistener.ui.components.ListStatusTabRow
import dev.eliaschen.notificationlistener.ui.components.NotificationListTopBar
import dev.eliaschen.notificationlistener.ui.components.SwipeableNotificationItem
import dev.eliaschen.notificationlistener.util.launchPackage
import dev.eliaschen.notificationlistener.viewmodel.NotificationViewModel

enum class NotificationTab(val label: String, val icon: ImageVector) {
    Active("Active", Icons.Default.Notifications),
    History("History", Icons.Default.History)
}

@Composable
fun NotificationList(viewModel: NotificationViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val activeNotification by viewModel.activeNotifications.collectAsStateWithLifecycle()
    val archivedNotifications by viewModel.archivedNotifications.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by remember { mutableStateOf(NotificationTab.entries.first()) }

    Scaffold(
        topBar = { NotificationListTopBar() },
        bottomBar = {
            ListStatusTabRow(selectedTab) {
                selectedTab = it
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        if (activeNotification.isEmpty()) {
            EmptyNotificationState(selectedTab)
        }
        LazyColumn(
            modifier = Modifier, contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            items(
                if (selectedTab == NotificationTab.Active) activeNotification else archivedNotifications,
                key = { it.id }) { item ->
                SwipeableNotificationItem(
                    item = item,
                    viewModel = viewModel,
                    snackbarHostState = snackbarHostState,
                    selectedTab = selectedTab,
                    onItemClick = { clickedItem ->
                        try {
                            val pendingIntent = viewModel.cache[clickedItem.id]
                            if (pendingIntent !== null) {
                                val options = ActivityOptions.makeBasic().apply {
                                    pendingIntentBackgroundActivityStartMode =
                                        ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                                }
                                pendingIntent.send(options.toBundle())
                            } else {
                                launchPackage(context, clickedItem.packageName)
                            }
                        } catch (e: Exception) {
                            launchPackage(context, clickedItem.packageName)
                        }
                    })
            }
        }
    }
}