package com.blackpirateapps.pause.data.model

import com.blackpirateapps.pause.data.local.MeditationPresetEntity
import com.blackpirateapps.pause.data.local.MeditationSessionEntity
import com.blackpirateapps.pause.domain.model.MeditationIcon
import com.blackpirateapps.pause.domain.model.MeditationPreset
import com.blackpirateapps.pause.domain.model.MeditationSession
import java.time.Instant
import java.time.LocalDate

fun MeditationPresetEntity.toDomain(): MeditationPreset =
    MeditationPreset(
        id = id,
        name = name,
        icon = MeditationIcon.fromId(icon),
        durationMinutes = durationMinutes,
        preparationSeconds = preparationSeconds,
        createdAt = Instant.ofEpochMilli(createdAtEpochMillis),
    )

fun MeditationSessionEntity.toDomain(): MeditationSession =
    MeditationSession(
        id = id,
        meditationId = meditationId,
        meditationName = meditationName,
        icon = MeditationIcon.fromId(icon),
        durationMinutes = durationMinutes,
        startedAt = Instant.ofEpochMilli(startedAtEpochMillis),
        completedAt = Instant.ofEpochMilli(completedAtEpochMillis),
        date = LocalDate.ofEpochDay(dateEpochDay),
    )

fun MeditationPreset.toSessionEntity(
    startedAt: Instant,
    completedAt: Instant,
    date: LocalDate,
): MeditationSessionEntity =
    MeditationSessionEntity(
        meditationId = id,
        meditationName = name,
        icon = icon.id,
        durationMinutes = durationMinutes,
        startedAtEpochMillis = startedAt.toEpochMilli(),
        completedAtEpochMillis = completedAt.toEpochMilli(),
        dateEpochDay = date.toEpochDay(),
    )
