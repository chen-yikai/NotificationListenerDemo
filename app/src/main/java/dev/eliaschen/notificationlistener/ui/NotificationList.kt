package dev.eliaschen.notificationlistener.ui

import android.app.ActivityOptions
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.eliaschen.notificationlistener.util.launchPackage
import dev.eliaschen.notificationlistener.util.toDateTime
import dev.eliaschen.notificationlistener.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch

@Composable
fun NotificationList(viewModel: NotificationViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        Surface(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 15.dp)
                .padding(top = 15.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainer,
            shadowElevation = 20.dp,
            border = CardDefaults.outlinedCardBorder()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Notification Listener Demo", style = MaterialTheme.typography.titleMedium)
            }
        }
    }, snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) { innerPadding ->
        if (notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No notifications yet",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        LazyColumn(
            modifier = Modifier.padding(innerPadding), contentPadding = PaddingValues(15.dp)
        ) {
            items(notifications, key = { it.id }) { item ->
                val dismissState = rememberSwipeToDismissBoxState(
                    positionalThreshold = { totalDistance -> totalDistance * 0.9f },
                )
                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromEndToStart = true,
                    enableDismissFromStartToEnd = true,
                    onDismiss = { value ->
                        when (value) {
                            SwipeToDismissBoxValue.EndToStart -> {
                                viewModel.deleteNotification(item)
                                scope.launch { snackbarHostState.showSnackbar("A notification has been deleted") }
                            }

                            SwipeToDismissBoxValue.StartToEnd -> {
                                viewModel.repostNotification(item)
                                scope.launch {
                                    dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                                    snackbarHostState.showSnackbar("Reposted to notification center")
                                }
                            }

                            else -> false
                        }
                    },
                    backgroundContent = {
                        val target = dismissState.targetValue
                        val isDelete = target == SwipeToDismissBoxValue.EndToStart
                        val isRepost = target == SwipeToDismissBoxValue.StartToEnd
                        val scale by animateFloatAsState(targetValue = if (isDelete || isRepost) 1.3f else 1f)
                        val color by animateColorAsState(
                            targetValue = when {
                                isDelete -> MaterialTheme.colorScheme.error
                                isRepost -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )

                        Surface(
                            Modifier
                                .padding(vertical = 5.dp)
                                .fillMaxSize(),
                            color = color,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                if (!isDelete) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.CenterStart)
                                            .padding(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = "Repost",
                                            modifier = Modifier.scale(scale)
                                        )
                                    }
                                }
                                if (!isRepost) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            modifier = Modifier.scale(scale)
                                        )
                                    }
                                }
                            }
                        }
                    }) {
                    Card(
                        modifier = Modifier.padding(vertical = 5.dp), onClick = {
                            try {
                                val pendingIntent = viewModel.cache[item.id]
                                if (pendingIntent !== null) {
                                    val options = ActivityOptions.makeBasic().apply {
                                        pendingIntentBackgroundActivityStartMode =
                                            ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                                    }
                                    pendingIntent.send(options.toBundle())
                                } else {
                                    launchPackage(context, item.packageName)
                                }
                            } catch (e: Exception) {
                                launchPackage(context, item.packageName)
                            }
                        }) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                bitmap = item.icon.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(item.text, style = MaterialTheme.typography.bodyMedium)
                            }
                            Text(
                                text = item.timestamp.toDateTime(),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}