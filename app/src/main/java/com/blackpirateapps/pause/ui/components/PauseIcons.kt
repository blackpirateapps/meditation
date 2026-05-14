package com.blackpirateapps.pause.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Park
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Waves
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.blackpirateapps.pause.domain.model.MeditationIcon

fun MeditationIcon.imageVector(): ImageVector =
    when (this) {
        MeditationIcon.Lotus -> Icons.Rounded.SelfImprovement
        MeditationIcon.Moon -> Icons.Rounded.NightsStay
        MeditationIcon.Breath -> Icons.Rounded.Air
        MeditationIcon.Waves -> Icons.Rounded.Waves
        MeditationIcon.Forest -> Icons.Rounded.Park
        MeditationIcon.Focus -> Icons.Rounded.CenterFocusStrong
        MeditationIcon.Gratitude -> Icons.Rounded.Favorite
        MeditationIcon.Sleep -> Icons.Rounded.Bedtime
    }

@Composable
fun MeditationIconBadge(
    icon: MeditationIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
) {
    Surface(
        modifier = modifier.size(size),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon.imageVector(),
                contentDescription = contentDescription,
                modifier = Modifier.size(size * 0.48f),
            )
        }
    }
}

@Composable
fun SmallMeditationIconBadge(
    icon: MeditationIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    MeditationIconBadge(
        icon = icon,
        contentDescription = contentDescription,
        modifier = modifier,
        size = 44.dp,
    )
}
