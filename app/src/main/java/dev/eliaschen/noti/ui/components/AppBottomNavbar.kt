package dev.eliaschen.noti.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.eliaschen.noti.ui.ReminderAction
import dev.eliaschen.noti.ui.ReminderMethod
import dev.eliaschen.noti.ui.Screen
import dev.eliaschen.noti.utils.BottomNav
import dev.eliaschen.noti.utils.LocalNavStack
import dev.eliaschen.noti.utils.SpotlightAction

private val navButtonWidth = 100.dp
private val navButtonHeight = 70.dp

@Composable
fun AppBottomNavbar() {
    val density = LocalDensity.current
    var navButtonOffsetX by remember { mutableFloatStateOf(0f) }
    val navStack = LocalNavStack.current
    val navIndicatorOffsetX by animateDpAsState(
        targetValue = if (navStack.last() == Screen.Notification) 0.dp else with(density) { navButtonOffsetX.toDp() },
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 200f
        ), label = "nav indicator offsetX"
    )
    var active by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        SpotlightAction.entries.forEachIndexed { index, item ->
            ActionButton(
                item,
                active,
                modifier = Modifier.align(Alignment.BottomCenter),
                index = index
            ) { active = false }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Card(
                border = CardDefaults.outlinedCardBorder(),
                shape = CircleShape,
                elevation = CardDefaults.outlinedCardElevation(5.dp), modifier = Modifier.align(
                    Alignment.BottomCenter
                )
            ) {
                Box {
                    Card(
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                        modifier = Modifier
                            .padding(5.dp)
                            .size(navButtonWidth, navButtonHeight)
                            .offset(x = navIndicatorOffsetX),
                        shape = CircleShape
                    ) { }
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                            .width(290.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNav.entries.forEach { item ->
                            NavButton(item, { active = false }) {
                                navButtonOffsetX = it
                            }
                        }
                    }
                }
            }
            SpotlightActionButton(active = active) { active = !active }
        }
    }
}

@Composable
private fun NavButton(item: BottomNav, dismiss: () -> Unit, onPositioned: (Float) -> Unit) {
    val navStack = LocalNavStack.current
    Column(
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                dismiss()
                navStack.clear()
                navStack.add(item.route)
            }
            .size(navButtonWidth, navButtonHeight)
            .onGloballyPositioned { coordinates ->
                onPositioned(coordinates.positionInParent().x)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(item.icon, contentDescription = item.label)
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            item.label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun SpotlightActionButton(
    modifier: Modifier = Modifier,
    active: Boolean = false,
    toggle: () -> Unit
) {
    val rotate by animateFloatAsState(
        targetValue = if (active) 135f else 0f,
        label = "spotlight action button rotation"
    )

    Box(
        modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable(onClick = toggle)
    ) {
        Icon(
            Icons.Rounded.Add,
            contentDescription = "Add",
            modifier = Modifier
                .size(50.dp)
                .rotate(rotate)
        )
    }
}

@Composable
private fun ActionButton(
    item: SpotlightAction,
    active: Boolean,
    index: Int,
    modifier: Modifier = Modifier,
    dismiss: () -> Unit,
) {
    val backStack = LocalNavStack.current
    val side = if (index == 0) -1 else 1
    val offsetY by animateDpAsState(
        targetValue = if (active) (-100).dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "action button offsetY"
    )
    val offsetX by animateDpAsState(
        if (active) (side * 60).dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "action button offsetX"
    )
    val alpha by animateFloatAsState(
        targetValue = if (active) 1f else 0f,
        label = "action button alpha"
    )
    val shadow by animateDpAsState(if (active && alpha == 1f) 5.dp else 0.dp)

    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.elevatedCardElevation(shadow),
        border = CardDefaults.outlinedCardBorder(),
        onClick = {
            dismiss()
            backStack.clear()
            when (item) {
                SpotlightAction.Task -> {
                    backStack.add(
                        Screen.Reminder(
                            method = ReminderMethod.Task,
                            action = ReminderAction.NewTask,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }

                SpotlightAction.Memo -> {
                    backStack.add(
                        Screen.Reminder(
                            method = ReminderMethod.Memo,
                            action = ReminderAction.NewMemo,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            }
        },
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .graphicsLayer { this.alpha = alpha }
            .size(80.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(item.icon, contentDescription = item.label)
            Spacer(modifier = Modifier.height(5.dp))
            Text(item.label, style = MaterialTheme.typography.labelMedium)
        }
    }
}