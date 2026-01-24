package dev.eliaschen.noti.ui

import android.app.ActivityOptions
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.eliaschen.noti.ui.components.EmptyNotificationState
import dev.eliaschen.noti.ui.components.SingleChooseTabRow
import dev.eliaschen.noti.ui.components.SwipeableNotificationItem
import dev.eliaschen.noti.utils.NotificationTab
import dev.eliaschen.noti.utils.launchPackage
import dev.eliaschen.noti.viewmodel.NotificationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activeNotification by viewModel.activeNotifications.collectAsStateWithLifecycle()
    val archivedNotifications by viewModel.archivedNotifications.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(NotificationTab.entries.first()) }
    val notifications =
        if (selectedTab == NotificationTab.Active) activeNotification else archivedNotifications

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background,
                                Color.Transparent
                            )
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
            ) {
                SingleChooseTabRow(
                    selectedTab
                ) {
                    selectedTab = it
                }
            }
        },
    ) { innerPadding ->
        if (notifications.isEmpty()) {
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
                notifications,
                key = { it.id }) { item ->
                SwipeableNotificationItem(
                    modifier = Modifier.animateItem(
                        fadeInSpec = spring(),
                        fadeOutSpec = spring(),
                        placementSpec = spring()
                    ),
                    item = item,
                    viewModel = viewModel,
                    selectedTab = selectedTab,
                    onItemClick = { clickedItem ->
                        // handle notification click (cache exist -> launch OG pendingIntent, else -> launch App via packageName)
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
                        // Archive notification after intent
                        if (selectedTab == NotificationTab.Active) {
                            viewModel.archiveNotification(clickedItem.id)
                        }
                    })
            }
        }
    }
}