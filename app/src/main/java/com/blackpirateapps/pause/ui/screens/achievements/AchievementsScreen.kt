package com.blackpirateapps.pause.ui.screens.achievements

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blackpirateapps.pause.domain.model.Achievement
import com.blackpirateapps.pause.ui.components.LoadingPane
import com.blackpirateapps.pause.ui.components.MeditationIconBadge
import com.blackpirateapps.pause.ui.components.ScreenHeader

@Composable
fun AchievementsScreen(viewModel: AchievementsViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AchievementsContent(state = state)
}

@Composable
private fun AchievementsContent(state: AchievementsUiState) {
    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp,
                ),
        ) {
            LoadingPane()
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 164.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp,
            end = 20.dp,
            bottom = 160.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
            ScreenHeader(
                title = "Achievements",
                subtitle = "Milestones that unlock from your real session history.",
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }
        items(
            items = state.achievements,
            key = { it.id },
        ) { achievement ->
            AchievementCard(achievement = achievement)
        }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    val infiniteTransition = rememberInfiniteTransition(label = "achievementPulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (achievement.unlocked) 1.025f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "achievementScale",
    )
    val containerColor = if (achievement.unlocked) {
        MaterialTheme.colorScheme.tertiaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.72f)
    }
    val contentColor = if (achievement.unlocked) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(pulse)
            .alpha(if (achievement.unlocked) 1f else 0.78f),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (achievement.unlocked) {
                    MeditationIconBadge(
                        icon = achievement.icon,
                        contentDescription = "${achievement.icon.label} achievement icon",
                        size = 52.dp,
                    )
                } else {
                    Surface(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.surface,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = "Locked achievement",
                            modifier = Modifier
                                .padding(14.dp)
                                .size(24.dp),
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Rounded.EmojiEvents,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = contentColor,
                )
            }

            Text(
                text = achievement.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
            )
            Text(
                text = achievement.description,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor.copy(alpha = 0.82f),
            )

            Spacer(modifier = Modifier.height(2.dp))
            LinearProgressIndicator(
                progress = { achievement.progressFraction },
                modifier = Modifier.fillMaxWidth(),
                color = if (achievement.unlocked) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.primary
                },
                trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
            )
            Text(
                text = "${achievement.progress.coerceAtMost(achievement.target)} / ${achievement.target}",
                style = MaterialTheme.typography.labelMedium,
                color = contentColor.copy(alpha = 0.82f),
            )
        }
    }
}
