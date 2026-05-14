package com.blackpirateapps.pause.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpirateapps.pause.data.repository.MeditationRepository
import com.blackpirateapps.pause.domain.model.DailyMeditationTotal
import com.blackpirateapps.pause.domain.model.MeditationSession
import com.blackpirateapps.pause.domain.model.MeditationStats
import com.blackpirateapps.pause.domain.model.StatsRange
import com.blackpirateapps.pause.domain.usecase.StatsCalculator
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StatsUiState(
    val stats: MeditationStats = MeditationStats(),
    val selectedRange: StatsRange = StatsRange.SevenDays,
    val dailyTotals: List<DailyMeditationTotal> = emptyList(),
    val isLoading: Boolean = true,
)

class StatsViewModel(
    repository: MeditationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState = _uiState.asStateFlow()
    private var sessions: List<MeditationSession> = emptyList()

    init {
        viewModelScope.launch {
            repository.sessions.collect { newSessions ->
                sessions = newSessions
                recalculate()
            }
        }
    }

    fun selectRange(range: StatsRange) {
        _uiState.update { it.copy(selectedRange = range) }
        recalculate()
    }

    private fun recalculate() {
        val today = LocalDate.now()
        val range = _uiState.value.selectedRange
        _uiState.update {
            it.copy(
                stats = StatsCalculator.calculate(sessions, today),
                dailyTotals = StatsCalculator.dailyTotalsForRange(sessions, range, today),
                isLoading = false,
            )
        }
    }
}
