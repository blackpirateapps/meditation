package com.blackpirateapps.pause.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blackpirateapps.pause.data.repository.MeditationRepository
import com.blackpirateapps.pause.ui.screens.achievements.AchievementsViewModel
import com.blackpirateapps.pause.ui.screens.history.HistoryViewModel
import com.blackpirateapps.pause.ui.screens.meditations.MeditationsViewModel
import com.blackpirateapps.pause.ui.screens.stats.StatsViewModel

class PauseViewModelFactory(
    private val repository: MeditationRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(MeditationsViewModel::class.java) ->
                MeditationsViewModel(repository) as T

            modelClass.isAssignableFrom(StatsViewModel::class.java) ->
                StatsViewModel(repository) as T

            modelClass.isAssignableFrom(HistoryViewModel::class.java) ->
                HistoryViewModel(repository) as T

            modelClass.isAssignableFrom(AchievementsViewModel::class.java) ->
                AchievementsViewModel(repository) as T

            else -> error("Unknown ViewModel class ${modelClass.name}")
        }
}
