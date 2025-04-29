package com.absut.cash.management.util

import com.absut.cash.management.ui.entrydetail.EntryType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.getFormattedDate(): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(this))
}

fun Int.toEntryType(): EntryType {
    return when (this) {
        EntryType.CASH_IN.value -> EntryType.CASH_IN
        EntryType.CASH_OUT.value -> EntryType.CASH_OUT
        else -> throw IllegalArgumentException("Invalid entry type value: $this")
    }
}