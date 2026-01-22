package dev.eliaschen.notificationlistener.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.eliaschen.notificationlistener.ui.NotificationTab
import dev.eliaschen.notificationlistener.ui.theme.notoSerif


@Composable
fun ListStatusTabRow(
    selectedTab: NotificationTab,
    modifier: Modifier = Modifier,
    onTabChange: (NotificationTab) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
    ) {
        NotificationTab.entries.forEachIndexed { index, item ->
            SegmentedButton(
                selected = selectedTab == item,
                onClick = { onTabChange(item) },
                colors = SegmentedButtonDefaults.colors(inactiveContainerColor = MaterialTheme.colorScheme.background),
                shape = SegmentedButtonDefaults.itemShape(
                    index, NotificationTab.entries.size, RoundedCornerShape(10.dp)
                ),
                icon = {
                    Icon(item.icon, contentDescription = item.label)
                }
            ) {
                Text(item.label, fontFamily = notoSerif)
            }
        }
    }
}