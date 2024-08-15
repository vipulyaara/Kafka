package ui.common.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val brandBlue = Color(0xff0067DD)
val white = Color(0xFFFFFFFF)
val whiteCream = Color(0xFFFAFAFA)
val darkGrey800 = Color(0xff121212)
val darkSurface = Color(0xFF22242C)
val textSecondaryDark = Color(0xff888888)
val Asphalt = Color(0xFF2c3e50)

val error = Color(0xffBA0550)

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
    primary = brandBlue,
    secondary = textSecondaryDark,
    background = darkGrey800,
    surface = darkSurface,
    tertiary = Color(0xffCFA224),
    error = error,
    secondaryContainer = darkSurface,
    onBackground = whiteCream,
    onSurface = whiteCream,
    onError = white,
    outline = whiteCream,
    onPrimary = white,
    onSecondary = white,
)

val LightAppColors = lightColorScheme(
    primary = brandBlue,
    secondary = Asphalt,
    background = whiteCream,
    surface = white,
    tertiary = Color(0xffCFA224),
    tertiaryContainer = brandBlue.copy(alpha = 0.1f),
    secondaryContainer = brandBlue.copy(alpha = 0.1f),
    error = error,
    onPrimary = white,
    onSecondary = white,
    onBackground = darkGrey800,
    onSurface = darkGrey800,
    onError = white,
    outline = darkGrey800
)
