package com.blackpirateapps.pause

import com.blackpirateapps.pause.domain.model.MeditationIcon
import com.blackpirateapps.pause.domain.model.MeditationSession
import com.blackpirateapps.pause.domain.model.StatsRange
import com.blackpirateapps.pause.domain.usecase.StatsCalculator
import java.time.Instant
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class StatsCalculatorTest {
    @Test
    fun calculatesTotalsAndUniqueDays() {
        val today = LocalDate.of(2026, 5, 14)
        val sessions = listOf(
            session(date = today, minutes = 10),
            session(date = today, minutes = 20),
            session(date = today.minusDays(2), minutes = 30),
        )

        val stats = StatsCalculator.calculate(sessions, today)

        assertEquals(60, stats.totalMinutes)
        assertEquals(2, stats.meditatedDays)
        assertEquals(30.0, stats.averageMinutesPerMeditatedDay, 0.001)
        assertEquals(3, stats.completedSessions)
    }

    @Test
    fun currentStreakEndsYesterdayWhenTodayHasNoSession() {
        val today = LocalDate.of(2026, 5, 14)
        val dates = setOf(
            today.minusDays(1),
            today.minusDays(2),
            today.minusDays(3),
            today.minusDays(5),
        )

        assertEquals(3, StatsCalculator.currentStreak(dates, today))
    }

    @Test
    fun bestStreakUsesLongestHistoricalRun() {
        val today = LocalDate.of(2026, 5, 14)
        val dates = setOf(
            today.minusDays(8),
            today.minusDays(7),
            today.minusDays(6),
            today.minusDays(2),
            today.minusDays(1),
        )

        assertEquals(3, StatsCalculator.bestStreak(dates))
    }

    @Test
    fun dailyTotalsAggregateMultipleSessionsInSelectedRange() {
        val today = LocalDate.of(2026, 5, 14)
        val sessions = listOf(
            session(date = today, minutes = 10),
            session(date = today, minutes = 5),
            session(date = today.minusDays(1), minutes = 20),
            session(date = today.minusDays(12), minutes = 45),
        )

        val totals = StatsCalculator.dailyTotalsForRange(
            sessions = sessions,
            range = StatsRange.SevenDays,
            today = today,
        )

        assertEquals(7, totals.size)
        assertEquals(20, totals.first { it.date == today.minusDays(1) }.minutes)
        assertEquals(15, totals.first { it.date == today }.minutes)
        assertEquals(0, totals.first { it.date == today.minusDays(6) }.minutes)
    }

    private fun session(date: LocalDate, minutes: Int): MeditationSession =
        MeditationSession(
            id = date.toEpochDay(),
            meditationId = 1,
            meditationName = "Morning",
            icon = MeditationIcon.Lotus,
            durationMinutes = minutes,
            startedAt = Instant.EPOCH,
            completedAt = Instant.EPOCH,
            date = date,
        )
}
