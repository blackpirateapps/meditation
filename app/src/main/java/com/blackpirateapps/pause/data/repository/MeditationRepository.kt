package com.blackpirateapps.pause.data.repository

import com.blackpirateapps.pause.data.local.MeditationPresetEntity
import com.blackpirateapps.pause.data.local.PauseDao
import com.blackpirateapps.pause.data.model.toDomain
import com.blackpirateapps.pause.data.model.toSessionEntity
import com.blackpirateapps.pause.domain.model.MeditationIcon
import com.blackpirateapps.pause.domain.model.MeditationPreset
import com.blackpirateapps.pause.domain.model.MeditationSession
import java.time.Clock
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MeditationRepository(
    private val dao: PauseDao,
    private val clock: Clock = Clock.systemDefaultZone(),
) {
    val presets: Flow<List<MeditationPreset>> = dao.observePresets()
        .map { presets -> presets.map { it.toDomain() } }

    val sessions: Flow<List<MeditationSession>> = dao.observeSessions()
        .map { sessions -> sessions.map { it.toDomain() } }

    suspend fun createPreset(
        name: String,
        icon: MeditationIcon,
        durationMinutes: Int,
        preparationSeconds: Int,
    ): Long {
        require(name.isNotBlank()) { "Name must not be blank." }
        require(durationMinutes > 0) { "Duration must be greater than 0 minutes." }
        require(preparationSeconds >= 0) { "Preparation time must be 0 seconds or greater." }

        return dao.insertPreset(
            MeditationPresetEntity(
                name = name.trim(),
                icon = icon.id,
                durationMinutes = durationMinutes,
                preparationSeconds = preparationSeconds,
                createdAtEpochMillis = clock.millis(),
            ),
        )
    }

    suspend fun completeSession(preset: MeditationPreset, startedAtMillis: Long): Long {
        val completedAt = clock.instant()
        val startedAt = java.time.Instant.ofEpochMilli(startedAtMillis)
        val date = LocalDate.now(clock)

        return dao.insertSession(
            preset.toSessionEntity(
                startedAt = startedAt,
                completedAt = completedAt,
                date = date,
            ),
        )
    }
}
