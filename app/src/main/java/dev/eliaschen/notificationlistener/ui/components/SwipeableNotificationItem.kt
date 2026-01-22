package dev.eliaschen.notificationlistener.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.SwipeToDismissBox
import dev.eliaschen.notificationlistener.room.NotificationEntity
import dev.eliaschen.notificationlistener.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch

@Composable
fun SwipeableNotificationItem(
    item: NotificationEntity,
    viewModel: NotificationViewModel,
    snackbarHostState: SnackbarHostState,
    onItemClick: (NotificationEntity) -> Unit
) {
    val scope = rememberCoroutineScope()
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
                    viewModel.archiveNotification(item.id)
                    scope.launch {
                        dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                        snackbarHostState.showSnackbar("A notification has been archived")
                    }
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
            SwipeDismissBackground(dismissState)
        }
    ) {
        NotificationItemCard(
            item = item,
            onItemClick = onItemClick
        )
    }
}
