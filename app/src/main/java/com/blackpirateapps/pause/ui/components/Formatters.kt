package com.blackpirateapps.pause.ui.components

import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt

private val fullDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
private val mediumDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
private val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

fun Int.minutesLabel(): String =
    when (this) {
        1 -> "1 min"
        else -> "$this min"
    }

fun Int.secondsLabel(): String =
    when (this) {
        1 -> "1 sec"
        else -> "$this sec"
    }

fun Double.averageMinutesLabel(): String =
    "${roundToInt()} min"

fun Int.totalTimeLabel(): String {
    val hours = this / 60
    val minutes = this % 60
    return when {
        hours == 0 -> minutes.minutesLabel()
        minutes == 0 -> "$hours h"
        else -> "$hours h $minutes min"
    }
}

fun Int.countdownLabel(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}

fun LocalDate.fullDateLabel(): String = format(fullDateFormatter)

fun LocalDate.mediumDateLabel(): String = format(mediumDateFormatter)

fun YearMonth.monthLabel(): String = format(monthFormatter)

fun Instant.timeLabel(): String =
    atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter)
