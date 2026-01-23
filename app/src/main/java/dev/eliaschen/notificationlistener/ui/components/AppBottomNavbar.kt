package dev.eliaschen.notificationlistener.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.eliaschen.notificationlistener.ui.Screen
import dev.eliaschen.notificationlistener.util.BottomNav
import dev.eliaschen.notificationlistener.util.LocalNavStack

@Composable
fun AppBottomNavbar() {
    val density = LocalDensity.current
    var navButtonOffsetX by remember { mutableStateOf(0f) }
    val navStack = LocalNavStack.current
    val navIndicatorOffsetX by animateDpAsState(
        targetValue = if (navStack.last() == Screen.Reminder) with(density) { navButtonOffsetX.toDp() } else 0.dp,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 200f
        ), label = "nav indicator offsetX"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            border = CardDefaults.outlinedCardBorder(),
            shape = CircleShape,
            elevation = CardDefaults.outlinedCardElevation(5.dp)
        ) {
            Box {
                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                    modifier = Modifier
                        .padding(5.dp)
                        .size(90.dp, 50.dp)
                        .offset(x = navIndicatorOffsetX),
                    shape = CircleShape
                ) { }
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .width(250.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNav.entries.forEach { item ->
                        NavButton(item) {
                            navButtonOffsetX = it
                        }
                    }
                }
            }
        }
        SpotlightActionButton(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun NavButton(item: BottomNav, onPositioned: (Float) -> Unit) {
    val navStack = LocalNavStack.current
    Column(
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                navStack.clear()
                navStack.add(item.route)
            }
            .size(90.dp, 50.dp)
            .onGloballyPositioned { coordinates ->
                onPositioned(coordinates.positionInParent().x)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(item.icon, contentDescription = item.label)
        Text(
            item.label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp
        )
    }
}

@Composable
fun SpotlightActionButton(modifier: Modifier = Modifier) {
    var active by remember { mutableStateOf(false) }
    val rotate by animateFloatAsState(
        targetValue = if (active) 45f else 0f,
        label = "spotlight action button rotation"
    )

    Box(
        modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable { active = !active }) {
        Icon(
            Icons.Rounded.Add,
            contentDescription = "Add",
            modifier = Modifier
                .size(40.dp)
                .rotate(rotate)
        )
    }
}