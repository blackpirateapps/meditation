package com.blackpirateapps.pause.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    Meditations("meditations", "Meditations", Icons.Rounded.Spa),
    Stats("stats", "Stats", Icons.Rounded.BarChart),
    History("history", "History", Icons.Rounded.CalendarMonth),
    Achievements("achievements", "Achievements", Icons.Rounded.EmojiEvents),
}
