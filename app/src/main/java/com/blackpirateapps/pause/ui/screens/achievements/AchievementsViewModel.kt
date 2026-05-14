package com.blackpirateapps.pause.ui.screens.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpirateapps.pause.data.repository.MeditationRepository
import com.blackpirateapps.pause.domain.model.Achievement
import com.blackpirateapps.pause.domain.usecase.AchievementCalculator
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AchievementsUiState(
    val achievements: List<Achievement> = emptyList(),
    val isLoading: Boolean = true,
)

class AchievementsViewModel(
    repository: MeditationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.sessions.collect { sessions ->
                _uiState.update {
                    it.copy(
                        achievements = AchievementCalculator.calculate(
                            sessions = sessions,
                            today = LocalDate.now(),
                        ),
                        isLoading = false,
                    )
                }
            }
        }
    }
}
