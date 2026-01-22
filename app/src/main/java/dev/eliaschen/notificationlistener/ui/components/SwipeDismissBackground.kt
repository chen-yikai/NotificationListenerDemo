package dev.eliaschen.notificationlistener.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun SwipeDismissBackground(dismissState: SwipeToDismissBoxState) {
    val target = dismissState.targetValue
    val isDelete = target == SwipeToDismissBoxValue.EndToStart
    val isRepost = target == SwipeToDismissBoxValue.StartToEnd
    val scale by animateFloatAsState(targetValue = if (isDelete || isRepost) 1.3f else 1f)
    val color by animateColorAsState(
        targetValue = when {
            isDelete -> MaterialTheme.colorScheme.tertiaryContainer
            isRepost -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surfaceContainer
        }
    )

    Surface(
        Modifier
            .padding(vertical = 5.dp)
            .fillMaxSize(),
        color = color,
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (!isDelete) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxSize()
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
                        .fillMaxSize()
                        .padding(20.dp), contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Archive,
                        contentDescription = "Delete",
                        modifier = Modifier.scale(scale)
                    )
                }
            }
        }
    }
}
