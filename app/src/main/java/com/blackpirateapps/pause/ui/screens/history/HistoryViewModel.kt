package com.blackpirateapps.pause.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpirateapps.pause.data.repository.MeditationRepository
import com.blackpirateapps.pause.domain.model.MeditationSession
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryDayDetails(
    val date: LocalDate,
    val sessions: List<MeditationSession>,
) {
    val totalMinutes: Int = sessions.sumOf { it.durationMinutes }
}

data class HistoryUiState(
    val displayedMonth: YearMonth = YearMonth.now(),
    val sessionsByDate: Map<LocalDate, List<MeditationSession>> = emptyMap(),
    val selectedDayDetails: HistoryDayDetails? = null,
    val isLoading: Boolean = true,
)

class HistoryViewModel(
    repository: MeditationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.sessions.collect { sessions ->
                _uiState.update {
                    it.copy(
                        sessionsByDate = sessions.groupBy { session -> session.date },
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun previousMonth() {
        _uiState.update { it.copy(displayedMonth = it.displayedMonth.minusMonths(1)) }
    }

    fun nextMonth() {
        _uiState.update { it.copy(displayedMonth = it.displayedMonth.plusMonths(1)) }
    }

    fun selectDate(date: LocalDate) {
        val sessions = _uiState.value.sessionsByDate[date].orEmpty()
        _uiState.update {
            it.copy(
                selectedDayDetails = if (sessions.isEmpty()) null else HistoryDayDetails(date, sessions),
            )
        }
    }

    fun dismissDayDetails() {
        _uiState.update { it.copy(selectedDayDetails = null) }
    }
}
