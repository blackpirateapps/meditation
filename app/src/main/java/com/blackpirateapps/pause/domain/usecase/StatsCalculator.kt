package com.blackpirateapps.pause.domain.usecase

import com.blackpirateapps.pause.domain.model.DailyMeditationTotal
import com.blackpirateapps.pause.domain.model.MeditationSession
import com.blackpirateapps.pause.domain.model.MeditationStats
import com.blackpirateapps.pause.domain.model.StatsRange
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object StatsCalculator {
    fun calculate(sessions: List<MeditationSession>, today: LocalDate): MeditationStats {
        val totalMinutes = sessions.sumOf { it.durationMinutes }
        val meditatedDates = sessions.map { it.date }.toSet()
        val meditatedDays = meditatedDates.size
        val average = if (meditatedDays == 0) 0.0 else totalMinutes.toDouble() / meditatedDays

        return MeditationStats(
            totalMinutes = totalMinutes,
            meditatedDays = meditatedDays,
            averageMinutesPerMeditatedDay = average,
            bestStreakDays = bestStreak(meditatedDates),
            currentStreakDays = currentStreak(meditatedDates, today),
            completedSessions = sessions.size,
        )
    }

    fun dailyTotalsForRange(
        sessions: List<MeditationSession>,
        range: StatsRange,
        today: LocalDate,
    ): List<DailyMeditationTotal> {
        if (sessions.isEmpty()) return emptyList()

        val totalsByDate = sessions
            .groupBy { it.date }
            .mapValues { (_, daySessions) -> daySessions.sumOf { it.durationMinutes } }

        val startDate = when (range) {
            StatsRange.SevenDays -> today.minusDays(6)
            StatsRange.TwoWeeks -> today.minusDays(13)
            StatsRange.OneMonth -> today.minusMonths(1).plusDays(1)
            StatsRange.SixMonths -> today.minusMonths(6).plusDays(1)
            StatsRange.OneYear -> today.minusYears(1).plusDays(1)
            StatsRange.All -> totalsByDate.keys.minOrNull() ?: today
        }

        if (startDate.isAfter(today)) return emptyList()

        val days = ChronoUnit.DAYS.between(startDate, today).toInt()
        return (0..days).map { offset ->
            val date = startDate.plusDays(offset.toLong())
            DailyMeditationTotal(date = date, minutes = totalsByDate[date] ?: 0)
        }
    }

    fun currentStreak(dates: Set<LocalDate>, today: LocalDate): Int {
        val streakEnd = when {
            today in dates -> today
            today.minusDays(1) in dates -> today.minusDays(1)
            else -> return 0
        }

        var cursor = streakEnd
        var count = 0
        while (cursor in dates) {
            count += 1
            cursor = cursor.minusDays(1)
        }
        return count
    }

    fun bestStreak(dates: Set<LocalDate>): Int {
        if (dates.isEmpty()) return 0

        var best = 1
        var current = 1
        val sortedDates = dates.sorted()

        for (index in 1 until sortedDates.size) {
            current = if (sortedDates[index - 1].plusDays(1) == sortedDates[index]) {
                current + 1
            } else {
                1
            }
            best = maxOf(best, current)
        }

        return best
    }
}
