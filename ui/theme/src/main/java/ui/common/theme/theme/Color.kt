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
    @Composable get() = if (isSystemInLightTheme()) {
        primary.copy(alpha = 0.5f)
    } else {
        primary.copy(alpha = 0.2f)
    }

@Composable
fun isSystemInLightTheme() = !isSystemInDarkTheme()

internal val DarkAppColors = darkColorScheme(
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

internal val LightAppColors = lightColorScheme(
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

val pastelColors = listOf(
    Color(0xFFD8A7B1), // Pastel pink
    Color(0xFFAACFCF), // Pastel blue
    Color(0xFFEFD9C1), // Pastel peach
    Color(0xFFB5EAD7), // Pastel green
    Color(0xFFDAC4F7), // Pastel lavender
    Color(0xFFF3D9E9), // Pastel rose
    Color(0xFFD5E5E7), // Pastel turquoise
    Color(0xFFE9EBF8), // Pastel lilac
    Color(0xFFEAD3CB), // Pastel coral
    Color(0xFFB9D1EA),  // Pastel sky blue
    Color(0xFFE2B6C9), // Pastel mauve
    Color(0xFFC3E6CB), // Pastel mint
    Color(0xFFE5D9AA), // Pastel yellow
    Color(0xFFC1C6E4), // Pastel periwinkle
    Color(0xFFE3D0BA), // Pastel apricot
    Color(0xFFC1E0E3), // Pastel aqua
    Color(0xFFF4DFE6), // Pastel blush
    Color(0xFFD8ECC3), // Pastel pistachio
    Color(0xFFE1D0F5), // Pastel lavender pink
    Color(0xFFF5E1D1),  // Pastel peach pink
    Color(0xFFC9E3E8), // Pastel sky
    Color(0xFFE0C9E3), // Pastel lavender
    Color(0xFFF2D7CB), // Pastel salmon
    Color(0xFFD5E8D4), // Pastel mint green
    Color(0xFFE8D2E2), // Pastel orchid
    Color(0xFFE3E8C9), // Pastel lime
    Color(0xFFF5E8D9), // Pastel peach
    Color(0xFFD9E5F5), // Pastel cornflower
    Color(0xFFF5E5D9), // Pastel beige
    Color(0xFFE5D9F5)  // Pastel violet
)
