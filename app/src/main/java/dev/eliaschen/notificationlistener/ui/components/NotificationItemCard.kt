package dev.eliaschen.notificationlistener.ui.components

import android.app.ActivityOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.eliaschen.notificationlistener.room.NotificationEntity
import dev.eliaschen.notificationlistener.util.launchPackage
import dev.eliaschen.notificationlistener.util.toDateTime

@Composable
fun NotificationItemCard(
    item: NotificationEntity,
    onItemClick: (NotificationEntity) -> Unit
) {
    Card(
        modifier = Modifier.padding(vertical = 5.dp),
        onClick = { onItemClick(item) }
    ) {
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
