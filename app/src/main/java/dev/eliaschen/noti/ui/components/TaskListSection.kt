package dev.eliaschen.noti.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.eliaschen.noti.room.table.TaskEntity

@Composable
fun TaskListSection(
    title: String,
    tasks: List<TaskEntity>,
    emptyMessage: String,
    onTaskCheckedChange: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    showCollapsibleHeader: Boolean = false,
    onHeaderClick: (() -> Unit)? = null,
    showBottomSpacer: Boolean = false
) {
    Box(modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 50.dp)
        ) {
            items(
                tasks,
                key = { it.id },
                contentType = { "task" }
            ) { task ->
                TaskCard(
                    task = task,
                    onCheckedChange = { checked ->
                        onTaskCheckedChange(task.id, checked)
                    },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .animateItem(
                            fadeInSpec = spring(stiffness = Spring.StiffnessLow),
                            fadeOutSpec = spring(stiffness = Spring.StiffnessLow),
                            placementSpec = spring(
                                stiffness = Spring.StiffnessLow,
                                dampingRatio = Spring.DampingRatioMediumBouncy
                            )
                        )
                )
            }
            if (showBottomSpacer) {
                item {
                    Spacer(
                        Modifier
                            .navigationBarsPadding()
                            .padding(30.dp)
                    )
                }
            }
        }
        
        if (tasks.isEmpty()) {
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Center)
            )
        }
        
        if (showCollapsibleHeader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .clickable { onHeaderClick?.invoke() }
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null)
            }
        } else {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background, Color.Transparent
                            )
                        )
                    )
                    .padding(top = 10.dp)
                    .padding(horizontal = 15.dp)
            )
        }
        
        HorizontalDivider(modifier = Modifier.align(Alignment.TopCenter))
    }
}
