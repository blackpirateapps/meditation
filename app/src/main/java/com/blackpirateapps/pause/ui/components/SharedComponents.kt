package com.blackpirateapps.pause.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ScreenHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingPane(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LoadingIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Settling in",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(18.dp)
                        .size(36.dp),
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun MetricPill(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    emphasized: Boolean = false,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = if (emphasized) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest
        },
        contentColor = if (emphasized) {
            MaterialTheme.colorScheme.onTertiaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (emphasized) {
                    MaterialTheme.colorScheme.onTertiaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
