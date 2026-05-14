package com.blackpirateapps.pause.domain.model

enum class AchievementId {
    FirstMeditation,
    ThreeDays,
    SevenDays,
    ThreeDayStreak,
    SevenDayStreak,
    SixtyMinutes,
    ThreeHundredMinutes,
    TenSessions,
}

data class Achievement(
    val id: AchievementId,
    val icon: MeditationIcon,
    val title: String,
    val description: String,
    val progress: Int,
    val target: Int,
) {
    val unlocked: Boolean = progress >= target
    val progressFraction: Float = (progress.toFloat() / target.toFloat()).coerceIn(0f, 1f)
}
