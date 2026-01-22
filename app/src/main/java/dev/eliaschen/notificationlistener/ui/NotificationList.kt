package dev.eliaschen.notificationlistener.ui

import android.app.ActivityOptions
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.eliaschen.notificationlistener.ui.components.EmptyNotificationState
import dev.eliaschen.notificationlistener.ui.components.NotificationListTopBar
import dev.eliaschen.notificationlistener.ui.components.SwipeableNotificationItem
import dev.eliaschen.notificationlistener.util.launchPackage
import dev.eliaschen.notificationlistener.viewmodel.NotificationViewModel

@Composable
fun NotificationList(viewModel: NotificationViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { NotificationListTopBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        if (notifications.isEmpty()) {
            EmptyNotificationState()
        }
        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            items(notifications, key = { it.id }) { item ->
                SwipeableNotificationItem(
                    item = item,
                    viewModel = viewModel,
                    snackbarHostState = snackbarHostState,
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
                    }
                )
            }
        }
    }
}