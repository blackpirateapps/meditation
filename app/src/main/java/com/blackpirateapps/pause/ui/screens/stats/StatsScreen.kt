package com.blackpirateapps.pause.ui.screens.stats

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blackpirateapps.pause.domain.model.DailyMeditationTotal
import com.blackpirateapps.pause.domain.model.MeditationStats
import com.blackpirateapps.pause.domain.model.StatsRange
import com.blackpirateapps.pause.ui.components.EmptyState
import com.blackpirateapps.pause.ui.components.LoadingPane
import com.blackpirateapps.pause.ui.components.MetricPill
import com.blackpirateapps.pause.ui.components.ScreenHeader
import com.blackpirateapps.pause.ui.components.averageMinutesLabel
import com.blackpirateapps.pause.ui.components.totalTimeLabel

@Composable
fun StatsScreen(viewModel: StatsViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    StatsContent(
        state = state,
        onRangeSelected = viewModel::selectRange,
    )
}

@Composable
private fun StatsContent(
    state: StatsUiState,
    onRangeSelected: (StatsRange) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp,
            end = 20.dp,
            bottom = 160.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            ScreenHeader(
                title = "Stats",
                subtitle = "Your practice over time, gathered quietly in the background.",
            )
        }

        when {
            state.isLoading -> item { LoadingPane() }
            state.stats.completedSessions == 0 -> item {
                EmptyState(
                    icon = Icons.Rounded.BarChart,
                    title = "No sessions yet",
                    message = "Complete a meditation to see totals, streaks, and your graph.",
                )
            }
            else -> {
                item {
                    StatsHero(stats = state.stats)
                }
                item {
                    RangeSelector(
                        selectedRange = state.selectedRange,
                        onRangeSelected = onRangeSelected,
                    )
                }
                item {
                    MeditationBarChart(dailyTotals = state.dailyTotals)
                }
            }
        }
    }
}

@Composable
private fun StatsHero(stats: MeditationStats) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stats.totalMinutes.totalTimeLabel(),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "total meditation time",
                style = MaterialTheme.typography.titleMedium,
            )
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricPill(
                        label = "Days",
                        value = stats.meditatedDays.toString(),
                        modifier = Modifier.weight(1f),
                    )
                    MetricPill(
                        label = "Avg",
                        value = stats.averageMinutesPerMeditatedDay.averageMinutesLabel(),
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricPill(
                        label = "Best",
                        value = "${stats.bestStreakDays}d",
                        modifier = Modifier.weight(1f),
                    )
                    MetricPill(
                        label = "Current",
                        value = "${stats.currentStreakDays}d",
                        modifier = Modifier.weight(1f),
                        emphasized = true,
                    )
                }
            }
        }
    }
}

@Composable
private fun RangeSelector(
    selectedRange: StatsRange,
    onRangeSelected: (StatsRange) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatsRange.entries.forEach { range ->
            FilterChip(
                selected = range == selectedRange,
                onClick = { onRangeSelected(range) },
                label = { Text(range.label) },
            )
        }
    }
}

@Composable
private fun MeditationBarChart(
    dailyTotals: List<DailyMeditationTotal>,
    modifier: Modifier = Modifier,
) {
    val maxMinutes = dailyTotals.maxOfOrNull { it.minutes } ?: 0
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val track = MaterialTheme.colorScheme.surfaceContainerHighest
    val description = if (maxMinutes == 0) {
        "Meditation chart with no recorded minutes in the selected range."
    } else {
        "Meditation chart showing daily totals. Highest day is $maxMinutes minutes."
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Duration by day",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (dailyTotals.isEmpty() || maxMinutes == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                ) {
                    Text(
                        text = "No meditation minutes in this range.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .semantics { contentDescription = description },
                ) {
                    val count = dailyTotals.size
                    val slotWidth = size.width / count
                    val barWidth = (slotWidth * 0.58f).coerceAtLeast(2f)
                    val corner = barWidth / 2f
                    val baseline = size.height

                    dailyTotals.forEachIndexed { index, total ->
                        val barHeight = (total.minutes.toFloat() / maxMinutes.toFloat()) * size.height
                        val left = index * slotWidth + (slotWidth - barWidth) / 2f
                        drawRoundRect(
                            color = track,
                            topLeft = Offset(left, 0f),
                            size = Size(barWidth, size.height),
                            cornerRadius = CornerRadius(corner, corner),
                        )
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    primary.copy(alpha = 0.92f),
                                    tertiary.copy(alpha = 0.42f),
                                ),
                            ),
                            topLeft = Offset(left, baseline - barHeight),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(corner, corner),
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = dailyTotals.firstOrNull()?.date?.month?.name.orEmpty().lowercase()
                        .replaceFirstChar { it.titlecase() },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = dailyTotals.lastOrNull()?.date?.month?.name.orEmpty().lowercase()
                        .replaceFirstChar { it.titlecase() },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
