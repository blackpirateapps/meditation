package com.blackpirateapps.pause.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meditation_sessions",
    indices = [
        Index("dateEpochDay"),
        Index("meditationId"),
    ],
)
data class MeditationSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val meditationId: Long?,
    val meditationName: String,
    val icon: String,
    val durationMinutes: Int,
    val startedAtEpochMillis: Long,
    val completedAtEpochMillis: Long,
    val dateEpochDay: Long,
)
