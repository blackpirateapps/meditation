package com.blackpirateapps.pause.domain.model

import java.time.Instant

data class MeditationPreset(
    val id: Long,
    val name: String,
    val icon: MeditationIcon,
    val durationMinutes: Int,
    val preparationSeconds: Int,
    val createdAt: Instant,
)
