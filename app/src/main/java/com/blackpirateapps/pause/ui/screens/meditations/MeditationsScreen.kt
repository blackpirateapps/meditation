package com.blackpirateapps.pause.ui.screens.meditations

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blackpirateapps.pause.domain.model.MeditationIcon
import com.blackpirateapps.pause.domain.model.MeditationPreset
import com.blackpirateapps.pause.ui.components.EmptyState
import com.blackpirateapps.pause.ui.components.LoadingPane
import com.blackpirateapps.pause.ui.components.MeditationIconBadge
import com.blackpirateapps.pause.ui.components.ScreenHeader
import com.blackpirateapps.pause.ui.components.SmallMeditationIconBadge
import com.blackpirateapps.pause.ui.components.countdownLabel
import com.blackpirateapps.pause.ui.components.imageVector
import com.blackpirateapps.pause.ui.components.minutesLabel
import com.blackpirateapps.pause.ui.components.secondsLabel
import kotlinx.coroutines.delay

@Composable
fun MeditationsScreen(viewModel: MeditationsViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    MeditationsContent(
        state = state,
        onStartMeditation = viewModel::startMeditation,
        onCancelSession = viewModel::cancelActiveSession,
        onTickSession = viewModel::tickActiveSession,
    )
}

@Composable
private fun MeditationsContent(
    state: MeditationsUiState,
    onStartMeditation: (MeditationPreset) -> Unit,
    onCancelSession: () -> Unit,
    onTickSession: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp,
                end = 20.dp,
                bottom = 160.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                ScreenHeader(
                    title = "Pause",
                    subtitle = "Choose a practice and let the next few minutes be simpler.",
                )
            }

            when {
                state.isLoading -> item { LoadingPane() }
                state.presets.isEmpty() -> item {
                    EmptyState(
                        icon = Icons.Rounded.Spa,
                        title = "No meditations yet",
                        message = "Create a preset with the button below to start tracking your practice.",
                    )
                }
                else -> items(
                    items = state.presets,
                    key = { it.id },
                ) { preset ->
                    MeditationPresetCard(
                        preset = preset,
                        onStartMeditation = { onStartMeditation(preset) },
                    )
                }
            }
        }
    }

    state.activeSession?.let { activeSession ->
        MeditationSessionDialog(
            activeSession = activeSession,
            onTickSession = onTickSession,
            onCancelSession = onCancelSession,
        )
    }
}

@Composable
private fun MeditationPresetCard(
    preset: MeditationPreset,
    onStartMeditation: () -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MeditationIconBadge(
                icon = preset.icon,
                contentDescription = "${preset.icon.label} meditation icon",
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = preset.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = preset.durationMinutes.minutesLabel(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (preset.preparationSeconds > 0) {
                        Text(
                            text = "Prep ${preset.preparationSeconds.secondsLabel()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            FilledTonalIconButton(
                onClick = onStartMeditation,
                modifier = Modifier.size(56.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = "Start ${preset.name}",
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMeditationSheet(
    onDismiss: () -> Unit,
    onSave: (String, MeditationIcon, Int, Int) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var iconId by rememberSaveable { mutableStateOf(MeditationIcon.default.id) }
    var durationText by rememberSaveable { mutableStateOf("10") }
    var preparationText by rememberSaveable { mutableStateOf("10") }

    val selectedIcon = MeditationIcon.fromId(iconId)
    val duration = durationText.toIntOrNull()
    val preparation = preparationText.toIntOrNull()
    val nameError = name.isNotEmpty() && name.isBlank()
    val durationError = durationText.isNotBlank() && (duration == null || duration <= 0)
    val preparationError = preparationText.isNotBlank() && (preparation == null || preparation < 0)
    val canSave = name.isNotBlank() &&
        duration != null &&
        duration > 0 &&
        preparation != null &&
        preparation >= 0

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "New meditation",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close create meditation",
                    )
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Name") },
                singleLine = true,
                isError = nameError,
                supportingText = {
                    if (nameError || name.isBlank()) {
                        Text("Name is required.")
                    }
                },
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Icon",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    MeditationIcon.entries.forEach { candidate ->
                        FilterChip(
                            selected = candidate == selectedIcon,
                            onClick = { iconId = candidate.id },
                            label = { Text(candidate.label) },
                            leadingIcon = {
                                Icon(
                                    imageVector = candidate.imageVector(),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                )
                            },
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = durationText,
                    onValueChange = { durationText = it.filter(Char::isDigit) },
                    modifier = Modifier.weight(1f),
                    label = { Text("Duration") },
                    suffix = { Text("min") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = durationError,
                    supportingText = {
                        if (durationError || durationText.isBlank()) {
                            Text("Use 1 or more.")
                        }
                    },
                )
                OutlinedTextField(
                    value = preparationText,
                    onValueChange = { preparationText = it.filter(Char::isDigit) },
                    modifier = Modifier.weight(1f),
                    label = { Text("Prep") },
                    suffix = { Text("sec") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = preparationError,
                    supportingText = {
                        if (preparationError || preparationText.isBlank()) {
                            Text("Use 0 or more.")
                        }
                    },
                )
            }

            Button(
                onClick = {
                    onSave(
                        name.trim(),
                        selectedIcon,
                        duration ?: 1,
                        preparation ?: 0,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                enabled = canSave,
            ) {
                Text("Save meditation")
            }
        }
    }
}

@Composable
private fun MeditationSessionDialog(
    activeSession: ActiveMeditationSession,
    onTickSession: () -> Unit,
    onCancelSession: () -> Unit,
) {
    LaunchedEffect(activeSession.phase, activeSession.remainingSeconds) {
        if (activeSession.phase != SessionPhase.Saving) {
            delay(1_000)
            onTickSession()
        }
    }

    Dialog(
        onDismissRequest = onCancelSession,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                SmallMeditationIconBadge(
                    icon = activeSession.preset.icon,
                    contentDescription = "${activeSession.preset.icon.label} meditation icon",
                )
                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = when (activeSession.phase) {
                        SessionPhase.Preparation -> "Prepare"
                        SessionPhase.Meditation -> activeSession.preset.name
                        SessionPhase.Saving -> "Session complete"
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = activeSession.remainingSeconds.countdownLabel(),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(24.dp))
                LinearProgressIndicator(
                    progress = {
                        if (activeSession.totalSeconds == 0) {
                            1f
                        } else {
                            1f - activeSession.remainingSeconds.toFloat() / activeSession.totalSeconds.toFloat()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                )
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = when (activeSession.phase) {
                        SessionPhase.Preparation -> "Let your body arrive before the timer begins."
                        SessionPhase.Meditation -> "Stay with the breath, then come back gently."
                        SessionPhase.Saving -> "Saving your practice."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(36.dp))
                Button(
                    onClick = onCancelSession,
                    enabled = activeSession.phase != SessionPhase.Saving,
                    modifier = Modifier.heightIn(min = 56.dp),
                ) {
                    Text("End session")
                }
            }
        }
    }
}
