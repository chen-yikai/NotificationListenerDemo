package dev.eliaschen.noti.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateTime(): String {
    val sdf = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    return sdf.format(Date(this))
}
