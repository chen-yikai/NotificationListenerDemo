package dev.eliaschen.noti.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.eliaschen.noti.utils.ExtraOption
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialog(
    dismiss: () -> Unit,
    onConfirm: (dateMillis: Long, timeMinutes: Long?) -> Unit = { _, _ -> },
    dateMillis: Long?,
    timeMinutes: Long?
) {
    var selectedDateTime by remember {
        mutableStateOf(
            if (dateMillis == null) LocalDateTime.now() else LocalDateTime.ofInstant(
                Instant.ofEpochMilli(dateMillis + (timeMinutes ?: 0) * 60 * 1000),
                ZoneId.systemDefault()
            )
        )
    }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var hasTimeSet by remember { mutableStateOf(timeMinutes !== null) }

    val dateTimeMillis = selectedDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val dateFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    val options = listOf(
        ExtraOption(
            "Date",
            Icons.Rounded.DateRange,
            timestamp = dateTimeMillis,
            format = "yyyy/MM/dd"
        ) { showDatePicker = true },
        ExtraOption(
            "Time",
            Icons.Rounded.AccessTime,
            timestamp = dateTimeMillis,
            format = if (hasTimeSet) "HH:mm" else ""
        ) { showTimePicker = true },
    )

    val formatters = mapOf(
        "yyyy/MM/dd" to dateFormatter,
        "HH:mm" to timeFormatter
    )

    if (showDatePicker) {
        CustomDatePickerDialog(
            initialDateMillis = dateTimeMillis,
            onDismiss = {
                showDatePicker = false
            }
        ) { dateMillis ->
            val newDate = Instant.ofEpochMilli(dateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            selectedDateTime = LocalDateTime.of(newDate, selectedDateTime.toLocalTime())
        }
    }
    if (showTimePicker) {
        CustomTimePickerDialog(
            initialHour = selectedDateTime.hour,
            initialMinute = selectedDateTime.minute,
            onDismiss = {
                showTimePicker = false
            },
            onClear = {
                hasTimeSet = false
                showTimePicker = false
            }
        ) { hour, minute ->
            selectedDateTime = selectedDateTime.withHour(hour).withMinute(minute)
            hasTimeSet = true
        }
    }
    Dialog(onDismissRequest = dismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
                items(options) { item ->
                    Row(
                        modifier = Modifier
                            .clickable(onClick = item.action)
                            .fillMaxWidth()
                            .padding(vertical = 15.dp, horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(item.icon, contentDescription = item.label)
                            Text(item.label, style = MaterialTheme.typography.titleMedium)
                        }
                        Text(
                            text = if (item.format.isEmpty()) "Not set" else formatters[item.format]?.format(
                                item.timestamp
                            ) ?: "",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (item.format.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else Color.Unspecified
                        )
                    }
                    HorizontalDivider()
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .fillMaxWidth()
                    ) {
                        TextButton(onClick = dismiss) { Text("Cancel") }
                        Spacer(Modifier.width(10.dp))
                        TextButton(onClick = {
                            // Get date at start of day (00:00:00)
                            val dateAtStartOfDay = selectedDateTime
                                .withHour(0).withMinute(0).withSecond(0).withNano(0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()

                            // Calculate time in minutes from midnight if time was set
                            val timeInMinutes = if (hasTimeSet) {
                                (selectedDateTime.hour * 60 + selectedDateTime.minute).toLong()
                            } else {
                                null
                            }

                            onConfirm(dateAtStartOfDay, timeInMinutes)
                            dismiss()
                        }) { Text("Done") }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDatePickerDialog(
    initialDateMillis: Long = System.currentTimeMillis(),
    onDismiss: () -> Unit,
    onSelected: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    onSelected(it)
                }
                onDismiss()
            }) {
                Text("Done")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = null,
            headline = null,
            showModeToggle = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onClear: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    onClear()
                    onDismiss()
                }) {
                    Text("Clear")
                }
                Row {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        onConfirm(timePickerState.hour, timePickerState.minute)
                        onDismiss()
                    }) {
                        Text("OK")
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = timePickerState)
            }
        }
    )
}