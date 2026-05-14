package com.blackpirateapps.pause.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.blackpirateapps.pause.data.repository.MeditationRepository
import com.blackpirateapps.pause.ui.screens.achievements.AchievementsScreen
import com.blackpirateapps.pause.ui.screens.achievements.AchievementsViewModel
import com.blackpirateapps.pause.ui.screens.history.HistoryScreen
import com.blackpirateapps.pause.ui.screens.history.HistoryViewModel
import com.blackpirateapps.pause.ui.screens.meditations.CreateMeditationSheet
import com.blackpirateapps.pause.ui.screens.meditations.MeditationsScreen
import com.blackpirateapps.pause.ui.screens.meditations.MeditationsViewModel
import com.blackpirateapps.pause.ui.screens.stats.StatsScreen
import com.blackpirateapps.pause.ui.screens.stats.StatsViewModel

@Composable
fun PauseApp(repository: MeditationRepository) {
    val navController = rememberNavController()
    val factory = PauseViewModelFactory(repository)
    val meditationsViewModel: MeditationsViewModel = viewModel(factory = factory)
    val statsViewModel: StatsViewModel = viewModel(factory = factory)
    val historyViewModel: HistoryViewModel = viewModel(factory = factory)
    val achievementsViewModel: AchievementsViewModel = viewModel(factory = factory)
    var showCreateSheet by rememberSaveable { mutableStateOf(false) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Screen.Meditations.route

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            if (currentRoute == Screen.Meditations.route) {
                FloatingActionButton(
                    onClick = { showCreateSheet = true },
                    modifier = Modifier.widthIn(min = 96.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Create meditation",
                    )
                }
            }
        },
        bottomBar = {
            FloatingBottomNavigation(
                currentRoute = currentRoute,
                onDestinationSelected = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Meditations.route,
            modifier = Modifier.padding(contentPadding),
        ) {
            composable(Screen.Meditations.route) {
                MeditationsScreen(viewModel = meditationsViewModel)
            }
            composable(Screen.Stats.route) {
                StatsScreen(viewModel = statsViewModel)
            }
            composable(Screen.History.route) {
                HistoryScreen(viewModel = historyViewModel)
            }
            composable(Screen.Achievements.route) {
                AchievementsScreen(viewModel = achievementsViewModel)
            }
        }
    }

    if (showCreateSheet) {
        CreateMeditationSheet(
            onDismiss = { showCreateSheet = false },
            onSave = { name, icon, durationMinutes, preparationSeconds ->
                meditationsViewModel.createPreset(
                    name = name,
                    icon = icon,
                    durationMinutes = durationMinutes,
                    preparationSeconds = preparationSeconds,
                )
                showCreateSheet = false
            },
        )
    }
}

@Composable
private fun FloatingBottomNavigation(
    currentRoute: String,
    onDestinationSelected: (Screen) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(40.dp)),
            shape = RoundedCornerShape(40.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.88f),
            contentColor = MaterialTheme.colorScheme.onSurface,
            shadowElevation = 8.dp,
            tonalElevation = 6.dp,
        ) {
            NavigationBar(
                modifier = Modifier.height(72.dp),
                containerColor = Color.Transparent,
                tonalElevation = 0.dp,
            ) {
                Screen.entries.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = { onDestinationSelected(screen) },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.label,
                            )
                        },
                        label = { Text(screen.label) },
                    )
                }
            }
        }
    }
}
