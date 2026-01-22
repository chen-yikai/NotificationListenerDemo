package dev.eliaschen.notificationlistener.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.eliaschen.notificationlistener.ui.theme.notoSerif
import dev.eliaschen.notificationlistener.util.BottomNav

@Composable
fun AppBottomNavbar() {
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
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .width(240.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNav.entries.forEach { item ->
                    NavButton(item)
                }
            }
        }
        SpotlightActionButton(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun NavButton(item: BottomNav) {
    Column(modifier = Modifier
        .clip(CircleShape)
        .clickable {}
        .padding(horizontal = 15.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(item.icon, contentDescription = item.label)
        Text(
            item.label,
            style = MaterialTheme.typography.labelSmall,
            fontFamily = notoSerif,
            fontSize = 10.sp
        )
    }
}

@Composable
fun SpotlightActionButton(modifier: Modifier = Modifier) {
    var active by remember { mutableStateOf(false) }
    val rotate by animateFloatAsState(targetValue = if (active) 45f else 0f)

    Box(
        modifier
            .clip(CircleShape)
            .clickable { active = !active }) {
        Icon(
            Icons.Rounded.Add,
            contentDescription = "Add",
            modifier = Modifier
                .size(45.dp)
                .rotate(rotate)
        )
    }
}