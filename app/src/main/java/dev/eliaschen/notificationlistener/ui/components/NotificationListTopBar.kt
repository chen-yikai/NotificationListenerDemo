package dev.eliaschen.notificationlistener.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationListTopBar() {
    Surface(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 15.dp)
            .padding(bottom = 10.dp)
            .padding(top = 15.dp),
        shape = RoundedCornerShape(20.dp),
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
}
