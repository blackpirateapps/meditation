package com.blackpirateapps.pause.domain.model

import java.time.LocalDate

data class MeditationStats(
    val totalMinutes: Int = 0,
    val meditatedDays: Int = 0,
    val averageMinutesPerMeditatedDay: Double = 0.0,
    val bestStreakDays: Int = 0,
    val currentStreakDays: Int = 0,
    val completedSessions: Int = 0,
)

data class DailyMeditationTotal(
    val date: LocalDate,
    val minutes: Int,
)

enum class StatsRange(val label: String) {
    SevenDays("7D"),
    TwoWeeks("2W"),
    OneMonth("1M"),
    SixMonths("6M"),
    OneYear("1Y"),
    All("All"),
}
