package com.blackpirateapps.pause

import android.app.Application
import com.blackpirateapps.pause.data.local.PauseDatabase
import com.blackpirateapps.pause.data.repository.MeditationRepository

class PauseApplication : Application() {
    val repository: MeditationRepository by lazy {
        MeditationRepository(PauseDatabase.getInstance(this).pauseDao())
    }
}
