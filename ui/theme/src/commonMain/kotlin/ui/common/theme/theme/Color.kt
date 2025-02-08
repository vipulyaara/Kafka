package ui.common.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class ThemeColor {
    Light, Dark;

    val isDark: Boolean
        get() = this == Dark
}

val LocalThemeColor = staticCompositionLocalOf<ThemeColor> {
    error("No LocalThemeColor given")
}

val ColorScheme.shadowMaterial
    @Composable get() = if (isSystemInDarkTheme()) {
        primary.copy(alpha = 0.2f)
    } else {
        primary.copy(alpha = 0.5f)
    }

val DarkAppColors = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFE0E0E0),
    onPrimaryContainer = Color.Black,
    inversePrimary = Color.Black,

    secondary = Color.White.copy(alpha = 0.7f),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFE0E0E0),
    onSecondaryContainer = Color.Black,

    tertiary = Color.White,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFE0E0E0),
    onTertiaryContainer = Color.Black,

    background = Color.Black,
    onBackground = Color.White,

    surface = Color.Black,
    onSurface = Color.White,

    surfaceVariant = Color(0xFF202020),
    onSurfaceVariant = Color.White,

    error = Color(0xFFeb0046),
    onError = Color.Black,
    errorContainer = Color(0xFFE0E0E0),
    onErrorContainer = Color.Black,

    outline = Color(0xFF2C2C2C),
    outlineVariant = Color.White,
    surfaceTint = Color.White
)

val LightAppColors = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF404040),
    onPrimaryContainer = Color.White,
    inversePrimary = Color.White,

    secondary = Color.Black.copy(alpha = 0.7f),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF404040),
    onSecondaryContainer = Color.White,

    tertiary = Color.Black,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF404040),
    onTertiaryContainer = Color.White,

    background = Color.White,
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black,

    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color.Black,

    error = Color(0xFFeb0046),
    onError = Color.White,
    errorContainer = Color(0xFF404040),
    onErrorContainer = Color.White,

    outline = Color(0xFFE0E0E0),
    outlineVariant = Color.Black,
    surfaceTint = Color.Black
)

val Yellow500 = Color(0xFF_EAB308)
val Rose800 = Color(0xFF9F1239)
val Slate50 = Color(0xFFFFFFFF)