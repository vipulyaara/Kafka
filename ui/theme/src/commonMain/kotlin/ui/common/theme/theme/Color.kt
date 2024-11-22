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

val ColorScheme.surfaceDeep
    @Composable get() = if (LocalTheme.current.isDark()) {
        Color.Black
    } else {
        Color.White
    }

val ColorScheme.inverseSurfaceDeep
    @Composable get() = if (LocalTheme.current.isDark()) {
        Color.White
    } else {
        Color.Black
    }

val ColorScheme.inverseOnSurfaceDeep
    @Composable get() = if (LocalTheme.current.isDark()) {
        Color.Black
    } else {
        Color.White
    }

val DarkAppColors = darkColorScheme(
    primary = Color(0xFFFFD69A),
    onPrimary = Color(0xFF341000),
    primaryContainer = Color(0xFFBD8701),
    onPrimaryContainer = Color(0xFF000000),
    inversePrimary = Color(0xFF4A3200),

    secondary = Color(0xFFFFD69A),
    onSecondary = Color(0xFF341000),
    secondaryContainer = Color(0xFFB48A3D),
    onSecondaryContainer = Color(0xFF000000),

    tertiary = Color(0xFFC9E79C),
    onTertiary = Color(0xFF192A00),
    tertiaryContainer = Color(0xFF7F9A57),
    onTertiaryContainer = Color(0xFF000000),

    background = Color(0xFF000000),
    onBackground = Color(0xFFE0E5D1),

    surface = Color(0xFF000000),
    onSurface = Color(0xFFFFFFFF),

    surfaceVariant = Color(0xFF414A33),
    onSurfaceVariant = Color(0xFFD6E0C1),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601414),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DDDB),

    outline = Color(0xFFACB699),
    surfaceTint = Color(0xFFFFD69A)
)

val LightAppColors = lightColorScheme(
    primary = Color(0xFF4A3200),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF906500),
    onPrimaryContainer = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFFFFD69A),

    secondary = Color(0xFF4A3200),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF8C661D),
    onSecondaryContainer = Color(0xFFFFFFFF),

    tertiary = Color(0xFF274006),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF5C7559),
    onTertiaryContainer = Color(0xFFFFFFFF),

    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF191D11),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0E1308),

    surfaceVariant = Color(0xFFDCF8C7),
    onSurfaceVariant = Color(0xFF303823),

    error = Color(0xFFB4261D),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DDDB),
    onErrorContainer = Color(0xFF410E0B),

    outline = Color(0xFF4C5640),
    surfaceTint = Color(0xFF4A3200)
)
