package com.blackpirateapps.pause.domain.usecase

import com.blackpirateapps.pause.domain.model.Achievement
import com.blackpirateapps.pause.domain.model.AchievementId
import com.blackpirateapps.pause.domain.model.MeditationIcon
import com.blackpirateapps.pause.domain.model.MeditationSession
import java.time.LocalDate

object AchievementCalculator {
    fun calculate(sessions: List<MeditationSession>, today: LocalDate): List<Achievement> {
        val stats = StatsCalculator.calculate(sessions, today)

        return listOf(
            Achievement(
                id = AchievementId.FirstMeditation,
                icon = MeditationIcon.Lotus,
                title = "First quiet minute",
                description = "Complete your first meditation.",
                progress = stats.completedSessions,
                target = 1,
            ),
            Achievement(
                id = AchievementId.ThreeDays,
                icon = MeditationIcon.Forest,
                title = "Three mindful days",
                description = "Meditate on 3 different days.",
                progress = stats.meditatedDays,
                target = 3,
            ),
            Achievement(
                id = AchievementId.SevenDays,
                icon = MeditationIcon.Waves,
                title = "Seven day rhythm",
                description = "Meditate on 7 different days.",
                progress = stats.meditatedDays,
                target = 7,
            ),
            Achievement(
                id = AchievementId.ThreeDayStreak,
                icon = MeditationIcon.Breath,
                title = "Gentle streak",
                description = "Reach a 3-day meditation streak.",
                progress = stats.bestStreakDays,
                target = 3,
            ),
            Achievement(
                id = AchievementId.SevenDayStreak,
                icon = MeditationIcon.Focus,
                title = "Steady practice",
                description = "Reach a 7-day meditation streak.",
                progress = stats.bestStreakDays,
                target = 7,
            ),
            Achievement(
                id = AchievementId.SixtyMinutes,
                icon = MeditationIcon.Moon,
                title = "One quiet hour",
                description = "Complete 60 total meditation minutes.",
                progress = stats.totalMinutes,
                target = 60,
            ),
            Achievement(
                id = AchievementId.ThreeHundredMinutes,
                icon = MeditationIcon.Sleep,
                title = "Deep reserve",
                description = "Complete 300 total meditation minutes.",
                progress = stats.totalMinutes,
                target = 300,
            ),
            Achievement(
                id = AchievementId.TenSessions,
                icon = MeditationIcon.Gratitude,
                title = "Ten returns",
                description = "Complete 10 meditation sessions.",
                progress = stats.completedSessions,
                target = 10,
            ),
        )
    }
}
