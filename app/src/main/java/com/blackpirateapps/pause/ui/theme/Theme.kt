package com.blackpirateapps.pause.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val PauseLightColors = lightColorScheme(
    primary = Color(0xFF426852),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC4EACF),
    onPrimaryContainer = Color(0xFF082014),
    secondary = Color(0xFF53655A),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD6E8DA),
    onSecondaryContainer = Color(0xFF111F17),
    tertiary = Color(0xFF42677A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC5E7F8),
    onTertiaryContainer = Color(0xFF001F2A),
    background = Color(0xFFF8FBF6),
    onBackground = Color(0xFF191D19),
    surface = Color(0xFFF8FBF6),
    onSurface = Color(0xFF191D19),
    surfaceVariant = Color(0xFFDDE5DB),
    onSurfaceVariant = Color(0xFF414940),
)

private val PauseDarkColors = darkColorScheme(
    primary = Color(0xFFA8D3B4),
    onPrimary = Color(0xFF113822),
    primaryContainer = Color(0xFF2A503B),
    onPrimaryContainer = Color(0xFFC4EACF),
    secondary = Color(0xFFBACCBF),
    onSecondary = Color(0xFF253328),
    secondaryContainer = Color(0xFF3B4B3F),
    onSecondaryContainer = Color(0xFFD6E8DA),
    tertiary = Color(0xFFA9CCDE),
    onTertiary = Color(0xFF123542),
    tertiaryContainer = Color(0xFF2B4E61),
    onTertiaryContainer = Color(0xFFC5E7F8),
    background = Color(0xFF111411),
    onBackground = Color(0xFFE1E4DE),
    surface = Color(0xFF111411),
    onSurface = Color(0xFFE1E4DE),
    surfaceVariant = Color(0xFF414940),
    onSurfaceVariant = Color(0xFFC1C9BE),
)

private val PauseShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PauseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && darkTheme ->
            dynamicDarkColorScheme(context)

        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            dynamicLightColorScheme(context)

        darkTheme -> PauseDarkColors
        else -> PauseLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = PauseShapes,
        motionScheme = MotionScheme.expressive(),
        content = content,
    )
}
