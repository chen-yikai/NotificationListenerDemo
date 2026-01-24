package dev.eliaschen.noti.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.eliaschen.noti.utils.TabRowData
import kotlin.enums.enumEntries


@Composable
inline fun <reified T> SingleChooseTabRow(
    selectedTab: T,
    modifier: Modifier = Modifier,
    noinline onTabChange: (T) -> Unit
) where T : Enum<T>, T : TabRowData {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
    ) {
        enumEntries<T>().forEachIndexed { index, item ->
            SegmentedButton(
                selected = selectedTab == item,
                onClick = { onTabChange(item) },
                colors = SegmentedButtonDefaults.colors(
                    inactiveContainerColor = MaterialTheme.colorScheme.background
                ),
                shape = SegmentedButtonDefaults.itemShape(
                    index, enumEntries<T>().size
                ),
                icon = {
                    Icon(item.icon, contentDescription = item.label)
                }
            ) {
                Text(item.label)
            }
        }
    }
}