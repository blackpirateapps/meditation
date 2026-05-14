package com.blackpirateapps.pause.domain.model

import java.time.Instant
import java.time.LocalDate

data class MeditationSession(
    val id: Long,
    val meditationId: Long?,
    val meditationName: String,
    val icon: MeditationIcon,
    val durationMinutes: Int,
    val startedAt: Instant,
    val completedAt: Instant,
    val date: LocalDate,
)
