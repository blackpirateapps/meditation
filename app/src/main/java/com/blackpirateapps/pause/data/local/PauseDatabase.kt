package com.blackpirateapps.pause.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        MeditationPresetEntity::class,
        MeditationSessionEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class PauseDatabase : RoomDatabase() {
    abstract fun pauseDao(): PauseDao

    companion object {
        @Volatile
        private var instance: PauseDatabase? = null

        fun getInstance(context: Context): PauseDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PauseDatabase::class.java,
                    "pause.db",
                ).build().also { instance = it }
            }
    }
}
