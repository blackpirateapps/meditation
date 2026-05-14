package com.blackpirateapps.pause

import com.blackpirateapps.pause.domain.model.AchievementId
import com.blackpirateapps.pause.domain.model.MeditationIcon
import com.blackpirateapps.pause.domain.model.MeditationSession
import com.blackpirateapps.pause.domain.usecase.AchievementCalculator
import java.time.Instant
import java.time.LocalDate
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AchievementCalculatorTest {
    @Test
    fun unlocksAchievementsFromPersistedSessionHistory() {
        val today = LocalDate.of(2026, 5, 14)
        val sessions = (0 until 10).map { index ->
            session(
                date = today.minusDays(index.toLong()),
                minutes = 30,
                id = index.toLong(),
            )
        }

        val achievements = AchievementCalculator.calculate(sessions, today)
            .associateBy { it.id }

        assertTrue(achievements.getValue(AchievementId.FirstMeditation).unlocked)
        assertTrue(achievements.getValue(AchievementId.SevenDays).unlocked)
        assertTrue(achievements.getValue(AchievementId.SevenDayStreak).unlocked)
        assertTrue(achievements.getValue(AchievementId.ThreeHundredMinutes).unlocked)
        assertTrue(achievements.getValue(AchievementId.TenSessions).unlocked)
    }

    @Test
    fun keepsAchievementsLockedUntilTargetsAreMet() {
        val today = LocalDate.of(2026, 5, 14)
        val sessions = listOf(session(date = today, minutes = 10, id = 1))

        val achievements = AchievementCalculator.calculate(sessions, today)
            .associateBy { it.id }

        assertTrue(achievements.getValue(AchievementId.FirstMeditation).unlocked)
        assertFalse(achievements.getValue(AchievementId.ThreeDays).unlocked)
        assertFalse(achievements.getValue(AchievementId.SixtyMinutes).unlocked)
        assertFalse(achievements.getValue(AchievementId.TenSessions).unlocked)
    }

    private fun session(date: LocalDate, minutes: Int, id: Long): MeditationSession =
        MeditationSession(
            id = id,
            meditationId = 1,
            meditationName = "Practice",
            icon = MeditationIcon.Breath,
            durationMinutes = minutes,
            startedAt = Instant.EPOCH,
            completedAt = Instant.EPOCH,
            date = date,
        )
}
