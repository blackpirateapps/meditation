package com.blackpirateapps.pause.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PauseDao {
    @Query("SELECT * FROM meditation_presets ORDER BY createdAtEpochMillis DESC")
    fun observePresets(): Flow<List<MeditationPresetEntity>>

    @Query("SELECT * FROM meditation_sessions ORDER BY completedAtEpochMillis DESC")
    fun observeSessions(): Flow<List<MeditationSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPreset(preset: MeditationPresetEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSession(session: MeditationSessionEntity): Long
}
