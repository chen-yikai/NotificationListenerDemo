package dev.eliaschen.noti.ui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.eliaschen.noti.utils.ExtraOption
import dev.eliaschen.noti.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBottomSheet(
    sheetState: SheetState,
    todo: TaskViewModel = hiltViewModel(),
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
    
    val timeFormatter = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
    val timeLabel = if (hasTime && time > 0) timeFormatter.format(time) else "Time"
    
    val timeIcon = if (hasTime) Icons.Rounded.Close else Icons.Rounded.AccessTime
    
    val options = listOf(
        ExtraOption("Details", Icons.Rounded.ShortText, hasDetails) { hasDetails = !hasDetails },
        ExtraOption(timeLabel, timeIcon, hasTime) { 
            if (hasTime) {
                time = 0L
                hasTime = false
            } else {
                showTimePicker = true
            }
        },
    )

    LaunchedEffect(Unit) {
        titleFocusRequester.requestFocus()
    }

    if (showTimePicker) {
        DateTimePickerDialog(
            dismiss = { showTimePicker = false },
            onConfirm = { selectedTimeMillis ->
                time = selectedTimeMillis
                hasTime = true
            }
        )
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
                    items(options) { item ->
                        AssistChip(
                            onClick = item.action,
                            label = { Text(item.label) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = if (item.active) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.background),
                            leadingIcon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.label
                                )
                            }
                        )
                    }
                }
                Button(onClick = {}) { Text("Save") }
            }
        }
    }
}