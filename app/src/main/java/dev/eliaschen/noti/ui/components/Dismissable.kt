package dev.eliaschen.noti.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class DismissableColor(
    val default: Color,
    val activeStart: Color,
    val activeEnd: Color
)

class DismissableScope(
    private val state: SwipeToDismissBoxState,
    val scope: CoroutineScope
) {
    fun dismiss() {
        scope.launch {
            state.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    fun reset() {
        scope.launch {
            state.reset()
        }
    }
}

@Composable
fun Dismissable(
    onStartToEndAction: (DismissableScope.() -> Unit)? = null,
    onEndToStartAction: (DismissableScope.() -> Unit)? = null,
    startContent: @Composable () -> Unit = {},
    endContent: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val backgroundColor = DismissableColor(
        default = MaterialTheme.colorScheme.surfaceContainerLow,
        activeStart = MaterialTheme.colorScheme.primaryContainer,
        activeEnd = MaterialTheme.colorScheme.errorContainer
    )
    val dismissState =
        rememberSwipeToDismissBoxState(positionalThreshold = { totalDistance -> totalDistance * 0.9f })
    val scopeInstance = DismissableScope(dismissState, scope)
    val swipeToEnd = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
    val swipeToStart = dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd
    val almostThere = swipeToEnd || swipeToStart
    val animatedColor by animateColorAsState(
        when {
            swipeToEnd -> backgroundColor.activeEnd
            swipeToStart -> backgroundColor.activeStart
            else -> backgroundColor.default
        }
    )
    val scale by animateFloatAsState(
        targetValue = if (almostThere) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .background(animatedColor)
                    .padding(horizontal = 20.dp)
            ) {
                if (!swipeToEnd)
                    Box(
                        modifier = Modifier
                            .scale(scale)
                            .align(Alignment.CenterStart)
                    ) {
                        startContent()
                    }


                if (!swipeToStart) Box(
                    modifier = Modifier
                        .scale(scale)
                        .align(Alignment.CenterEnd)
                ) {
                    endContent()
                }
            }
        },
        onDismiss = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onStartToEndAction?.invoke(scopeInstance)
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onEndToStartAction?.invoke(scopeInstance)
                }

                SwipeToDismissBoxValue.Settled -> {

                }
            }
        },
        enableDismissFromStartToEnd = onStartToEndAction !== null,
        enableDismissFromEndToStart = onEndToStartAction !== null
    ) {
        content()
    }
}