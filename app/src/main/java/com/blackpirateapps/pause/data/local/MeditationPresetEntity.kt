package com.blackpirateapps.pause.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meditation_presets")
data class MeditationPresetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String,
    val durationMinutes: Int,
    val preparationSeconds: Int,
    val createdAtEpochMillis: Long,
)
