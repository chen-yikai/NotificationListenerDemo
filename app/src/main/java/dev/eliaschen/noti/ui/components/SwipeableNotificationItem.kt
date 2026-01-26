package dev.eliaschen.noti.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.eliaschen.noti.room.table.NotificationEntity
import dev.eliaschen.noti.utils.LocalSnackBarHostState
import dev.eliaschen.noti.utils.NotificationTab
import dev.eliaschen.noti.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch

@Composable
fun SwipeableNotificationItem(
    modifier: Modifier = Modifier,
    item: NotificationEntity,
    viewModel: NotificationViewModel,
    onItemClick: (NotificationEntity) -> Unit,
    selectedTab: NotificationTab
) {
    val snackbarHostState = LocalSnackBarHostState.current
    val scope = rememberCoroutineScope()
    val isDelete = selectedTab == NotificationTab.History

    Dismissable(
        modifier = modifier.padding(vertical = 5.dp),
        onStartToEndAction = if (selectedTab == NotificationTab.Active) {
            {
                viewModel.repostNotification(item)
                scope.launch {
                    snackbarHostState.showSnackbar("Reposted to notification center")
                }
                dismiss()
            }
        } else null,
        onEndToStartAction = {
            if (isDelete) {
                viewModel.deleteNotification(item)
            } else {
                viewModel.archiveNotification(item.id)
            }
            dismiss()
        },
        startContent = {
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = "Repost"
            )
        },
        endContent = {
            Icon(
                imageVector = if (isDelete) Icons.Rounded.Delete else Icons.Rounded.Archive,
                contentDescription = if (isDelete) "Delete" else "Archive"
            )
        }
    ) {
        NotificationItemCard(
            item = item,
            onItemClick = onItemClick
        )
    }
}
