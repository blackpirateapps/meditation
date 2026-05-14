package com.blackpirateapps.pause.ui.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blackpirateapps.pause.domain.model.MeditationSession
import com.blackpirateapps.pause.ui.components.EmptyState
import com.blackpirateapps.pause.ui.components.LoadingPane
import com.blackpirateapps.pause.ui.components.ScreenHeader
import com.blackpirateapps.pause.ui.components.SmallMeditationIconBadge
import com.blackpirateapps.pause.ui.components.fullDateLabel
import com.blackpirateapps.pause.ui.components.mediumDateLabel
import com.blackpirateapps.pause.ui.components.minutesLabel
import com.blackpirateapps.pause.ui.components.monthLabel
import com.blackpirateapps.pause.ui.components.timeLabel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryContent(
        state = state,
        onPreviousMonth = viewModel::previousMonth,
        onNextMonth = viewModel::nextMonth,
        onSelectDate = viewModel::selectDate,
        onDismissDetails = viewModel::dismissDayDetails,
    )
}

@Composable
private fun HistoryContent(
    state: HistoryUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onDismissDetails: () -> Unit,
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
                title = "History",
                subtitle = "A month-by-month view of where your practice has touched the calendar.",
            )
        }

        when {
            state.isLoading -> item { LoadingPane() }
            state.sessionsByDate.isEmpty() -> item {
                EmptyState(
                    icon = Icons.Rounded.CalendarMonth,
                    title = "No history yet",
                    message = "Completed sessions will appear as softly marked days.",
                )
            }
            else -> item {
                CalendarMonthView(
                    month = state.displayedMonth,
                    sessionsByDate = state.sessionsByDate,
                    onPreviousMonth = onPreviousMonth,
                    onNextMonth = onNextMonth,
                    onSelectDate = onSelectDate,
                )
            }
        }
    }

    state.selectedDayDetails?.let { details ->
        DayDetailsSheet(
            details = details,
            onDismiss = onDismissDetails,
        )
    }
}

@Composable
private fun CalendarMonthView(
    month: YearMonth,
    sessionsByDate: Map<LocalDate, List<MeditationSession>>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onPreviousMonth) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronLeft,
                        contentDescription = "Previous month",
                    )
                }
                Text(
                    text = month.monthLabel(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                IconButton(onClick = onNextMonth) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = "Next month",
                    )
                }
            }

            WeekdayHeader()

            monthCells(month).chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    week.forEach { date ->
                        val sessions = if (date == null) emptyList() else sessionsByDate[date].orEmpty()
                        CalendarDayCell(
                            date = date,
                            sessionCount = sessions.size,
                            onSelectDate = onSelectDate,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekdayHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        listOf("M", "T", "W", "T", "F", "S", "S").forEach { label ->
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun CalendarDayCell(
    date: LocalDate?,
    sessionCount: Int,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasSessions = sessionCount > 0
    val description = when {
        date == null -> "Blank calendar day"
        hasSessions -> "${date.fullDateLabel()}, $sessionCount meditation sessions"
        else -> "${date.fullDateLabel()}, no meditation sessions"
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        if (date != null) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = hasSessions) { onSelectDate(date) },
                shape = MaterialTheme.shapes.extraLarge,
                color = if (hasSessions) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHighest
                },
                contentColor = if (hasSessions) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (hasSessions) FontWeight.SemiBold else FontWeight.Normal,
                    )
                    if (hasSessions) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            modifier = Modifier.size(8.dp),
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.primary,
                            content = {},
                        )
                    }
                }
            }
        }
    }
}

private fun monthCells(month: YearMonth): List<LocalDate?> {
    val firstDay = month.atDay(1)
    val leadingBlanks = firstDay.dayOfWeek.value - 1
    val days = (1..month.lengthOfMonth()).map { month.atDay(it) }
    val trailingBlanks = (7 - ((leadingBlanks + days.size) % 7)).let { if (it == 7) 0 else it }
    return List(leadingBlanks) { null } + days + List(trailingBlanks) { null }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DayDetailsSheet(
    details: HistoryDayDetails,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = details.date.mediumDateLabel(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${details.totalMinutes.minutesLabel()} across ${details.sessions.size} sessions",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            details.sessions.forEach { session ->
                SessionDetailCard(session = session)
            }
        }
    }
}

@Composable
private fun SessionDetailCard(session: MeditationSession) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SmallMeditationIconBadge(
                icon = session.icon,
                contentDescription = "${session.icon.label} session icon",
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.meditationName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = session.completedAt.timeLabel(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = session.durationMinutes.minutesLabel(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
