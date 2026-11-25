package com.project1k.money.ui

import java.util.Calendar
import java.util.Locale

private val MONTHS = arrayOf(
    "Jan", "Feb", "Mar", "Apr", "Maj", "Jun",
    "Jul", "Avg", "Sep", "Okt", "Nov", "Dec"
)

fun formatAppDate(timestamp: Long): String {
    val c = Calendar.getInstance().apply { timeInMillis = timestamp }
    val day = c.get(Calendar.DAY_OF_MONTH)
    val month = MONTHS[c.get(Calendar.MONTH)]
    val year = c.get(Calendar.YEAR) % 100
    return String.format(Locale.ROOT, "%d %s %02d", day, month, year)
}

fun formatAppDateTime(timestamp: Long): String {
    val c = Calendar.getInstance().apply { timeInMillis = timestamp }
    return String.format(
        Locale.ROOT,
        "%s · %02d:%02d",
        formatAppDate(timestamp),
        c.get(Calendar.HOUR_OF_DAY),
        c.get(Calendar.MINUTE)
    )
}

fun formatAppTime(timestamp: Long): String {
    val c = Calendar.getInstance().apply { timeInMillis = timestamp }
    return String.format(Locale.ROOT, "%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))
}
