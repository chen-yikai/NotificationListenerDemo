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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.eliaschen.noti.room.table.TaskEntity
import dev.eliaschen.noti.utils.ExtraOption
import dev.eliaschen.noti.viewmodel.TaskViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBottomSheet(
    sheetState: SheetState,
    todo: TaskViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()

    // text fields for title and info
    val title = rememberTextFieldState()
    val info = rememberTextFieldState()

    // focus requester for title and detail
    val titleFocusRequester = remember { FocusRequester() }
    val detailFocusRequester = remember { FocusRequester() }

    var hasDetails by remember { mutableStateOf(false) }

    // date & time's milliseconds since epoch data & pattern formatter
    var dateMillis by remember { mutableStateOf<Long?>(null) }
    var timeMinutes by remember { mutableStateOf<Long?>(null) }
    val dateFormatter = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    // control custom datetime picker dialog
    var showDateTimePicker by remember { mutableStateOf(false) }
    val timeLabel = when {
        dateMillis == null -> "Date & Time" // no date & time is selected
        timeMinutes == null -> dateFormatter.format(dateMillis) // only date is selected
        else -> { // both date & time are selected
            val calendar = Calendar.getInstance().apply { // extract time from timestamp
                timeInMillis = dateMillis!!
                set(Calendar.HOUR_OF_DAY, (timeMinutes!! / 60).toInt())
                set(Calendar.MINUTE, (timeMinutes!! % 60).toInt())
            }
            // final display label
            "${dateFormatter.format(dateMillis)}, ${timeFormatter.format(calendar.timeInMillis)}"
        }
    }

    // chips data
    val timeIcon = if (dateMillis != null) Icons.Rounded.Close else Icons.Rounded.AccessTime
    val options = listOf(
        ExtraOption("Details", Icons.Rounded.ShortText, hasDetails) {
            hasDetails = !hasDetails
            if (hasDetails) {
                scope.launch {
                    delay(100)
                    detailFocusRequester.requestFocus()
                }
            } else {
                scope.launch {
                    titleFocusRequester.requestFocus()
                }
            }
        },
        ExtraOption(timeLabel, timeIcon, dateMillis != null, extraAction = {
            dateMillis = null
            timeMinutes = null
        }) {
            showDateTimePicker = true
        },
    )

    LaunchedEffect(Unit) {
        // focus the title input as soon as the sheet is opened
        titleFocusRequester.requestFocus()
    }

    if (showDateTimePicker) {
        DateTimePickerDialog(
            dismiss = { showDateTimePicker = false },
            onConfirm = { selectedDateMillis, selectedTimeMinutes ->
                dateMillis = selectedDateMillis
                timeMinutes = selectedTimeMinutes
            }
        )
    }

    ModalBottomSheet(sheetState = sheetState, onDismissRequest = {
        scope.launch {
            onDismiss()
        }
    }) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 10.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val accentColor =
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f) // text color for input placeholder
            val titleTextStyle =
                MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            BasicTextField(
                title,
                textStyle = titleTextStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(titleFocusRequester),
                lineLimits = TextFieldLineLimits.SingleLine,
                cursorBrush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary),
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
                val infoTextStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
                BasicTextField(
                    info,
                    textStyle = infoTextStyle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(detailFocusRequester),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary),
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
                        val containerColor =
                            if (item.active) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        val labelColor =
                            if (item.active) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        AssistChip(
                            onClick = item.action,
                            label = { Text(item.label) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = containerColor,
                                labelColor = labelColor,
                                leadingIconContentColor = labelColor
                            ),
                            leadingIcon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.label,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .then(
                                            if (item.extraAction != null && item.active) Modifier.clickable(
                                                onClick = item.extraAction
                                            ) else Modifier
                                        ) // only allow extra action if leading icon is cancel
                                )
                            }
                        )
                    }
                }
                Button(onClick = {
                    val currentTime = System.currentTimeMillis()
                    val task = TaskEntity(
                        title = title.text.toString().ifEmpty { "Untitled" },
                        date = dateMillis,
                        time = timeMinutes,
                        detail = if (hasDetails) info.text.toString() else null,
                        createdAt = currentTime,
                        updatedAt = currentTime
                    )
                    todo.addTask(task)
                    scope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                }) { Text("Save") }
            }
        }
    }
}