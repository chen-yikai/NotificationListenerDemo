package dev.eliaschen.noti.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.eliaschen.noti.viewmodel.TodoViewModel
import kotlinx.coroutines.launch

data class ExtraOption(
    val label: String,
    val icon: ImageVector,
    val active: Boolean,
    val action: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBottomSheet(
    sheetState: SheetState,
    todo: TodoViewModel = hiltViewModel(),
    showBottomSheet: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val title = rememberTextFieldState()
    val info = rememberTextFieldState()
    val titleFocusRequester = remember { FocusRequester() }
    var time by remember { mutableStateOf(0L) }
    var hasTime by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var hasDetails by remember { mutableStateOf(false) }
    val options = listOf(
        ExtraOption("Details", Icons.Rounded.ShortText, hasDetails, { hasDetails = !hasDetails }),
        ExtraOption("Time", Icons.Rounded.AccessTime, hasTime, {

        }),
    )

    LaunchedEffect(Unit) {
        titleFocusRequester.requestFocus()
    }

    ModalBottomSheet(sheetState = sheetState, onDismissRequest = {
        scope.launch {
            showBottomSheet()
        }
    }) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 10.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val accentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            val titleTextStyle =
                MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            val infoTextStyle = MaterialTheme.typography.bodyLarge
            BasicTextField(
                title,
                textStyle = titleTextStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(titleFocusRequester),
                lineLimits = TextFieldLineLimits.SingleLine,
                decorator = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (title.text.isEmpty()) {
                            Text(
                                text = "Untitled",
                                style = titleTextStyle.copy(color = accentColor)
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (hasDetails) {
                BasicTextField(
                    info, textStyle = infoTextStyle, modifier = Modifier.fillMaxWidth(),
                    decorator = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (info.text.isEmpty()) {
                                Text(
                                    text = "some description...",
                                    style = infoTextStyle.copy(color = accentColor)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(options) { (label, icon, active, action) ->
                        AssistChip(
                            onClick = action,
                            label = { Text(label) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = if (active) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.background),
                            leadingIcon = {
                                Icon(
                                    icon,
                                    contentDescription = label
                                )
                            })
                    }
                }
                Button(onClick = {}) { Text("Save") }
            }
        }
    }
}