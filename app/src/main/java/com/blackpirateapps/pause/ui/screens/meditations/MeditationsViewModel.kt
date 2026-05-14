package com.blackpirateapps.pause.ui.screens.meditations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackpirateapps.pause.data.repository.MeditationRepository
import com.blackpirateapps.pause.domain.model.MeditationIcon
import com.blackpirateapps.pause.domain.model.MeditationPreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MeditationsUiState(
    val presets: List<MeditationPreset> = emptyList(),
    val isLoading: Boolean = true,
    val activeSession: ActiveMeditationSession? = null,
    val creationError: String? = null,
)

enum class SessionPhase {
    Preparation,
    Meditation,
    Saving,
}

data class ActiveMeditationSession(
    val preset: MeditationPreset,
    val phase: SessionPhase,
    val remainingSeconds: Int,
    val totalSeconds: Int,
    val startedAtMillis: Long,
)

class MeditationsViewModel(
    private val repository: MeditationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MeditationsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.presets.collect { presets ->
                _uiState.update {
                    it.copy(
                        presets = presets,
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun createPreset(
        name: String,
        icon: MeditationIcon,
        durationMinutes: Int,
        preparationSeconds: Int,
    ) {
        viewModelScope.launch {
            runCatching {
                repository.createPreset(
                    name = name,
                    icon = icon,
                    durationMinutes = durationMinutes,
                    preparationSeconds = preparationSeconds,
                )
            }.onSuccess {
                _uiState.update { it.copy(creationError = null) }
            }.onFailure { error ->
                _uiState.update { it.copy(creationError = error.message) }
            }
        }
    }

    fun startMeditation(preset: MeditationPreset) {
        val hasPreparation = preset.preparationSeconds > 0
        _uiState.update {
            it.copy(
                activeSession = ActiveMeditationSession(
                    preset = preset,
                    phase = if (hasPreparation) SessionPhase.Preparation else SessionPhase.Meditation,
                    remainingSeconds = if (hasPreparation) {
                        preset.preparationSeconds
                    } else {
                        preset.durationMinutes * 60
                    },
                    totalSeconds = if (hasPreparation) {
                        preset.preparationSeconds
                    } else {
                        preset.durationMinutes * 60
                    },
                    startedAtMillis = if (hasPreparation) 0L else System.currentTimeMillis(),
                ),
            )
        }
    }

    fun tickActiveSession() {
        val active = _uiState.value.activeSession ?: return
        when {
            active.phase == SessionPhase.Saving -> Unit
            active.remainingSeconds > 1 -> {
                _uiState.update {
                    it.copy(activeSession = active.copy(remainingSeconds = active.remainingSeconds - 1))
                }
            }
            active.phase == SessionPhase.Preparation -> {
                val meditationSeconds = active.preset.durationMinutes * 60
                _uiState.update {
                    it.copy(
                        activeSession = active.copy(
                            phase = SessionPhase.Meditation,
                            remainingSeconds = meditationSeconds,
                            totalSeconds = meditationSeconds,
                            startedAtMillis = System.currentTimeMillis(),
                        ),
                    )
                }
            }
            active.phase == SessionPhase.Meditation -> completeActiveSession(active)
        }
    }

    fun cancelActiveSession() {
        _uiState.update { it.copy(activeSession = null) }
    }

    private fun completeActiveSession(active: ActiveMeditationSession) {
        _uiState.update {
            it.copy(activeSession = active.copy(phase = SessionPhase.Saving, remainingSeconds = 0))
        }
        viewModelScope.launch {
            repository.completeSession(
                preset = active.preset,
                startedAtMillis = active.startedAtMillis,
            )
            _uiState.update { it.copy(activeSession = null) }
        }
    }
}
