package com.kafka.ui_common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
val KafkaColors
    get() = KafkaTheme.colors

@Composable
val KafkaTypography
    get() = MaterialTheme.typography

private val LightColorPalette = KafkaColorPalette(
    primary = primary,
    secondary = secondary,
    background = background,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    iconPrimary = iconPrimary,
    iconSecondary = iconPrimary,
    surface = surface,
    isDark = false
)

private val DarkColorPalette = KafkaColorPalette(
    primary = primaryDark,
    secondary = secondaryDark,
    background = backgroundDark,
    textPrimary = textPrimaryDark,
    textSecondary = textSecondaryDark,
    iconPrimary = iconPrimaryDark,
    iconSecondary = iconPrimaryDark,
    surface = surfaceDark,
    isDark = true
)

@Composable
fun KafkaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    val sysUiController = SysUiController.current
    onCommit(sysUiController, colors.background) {
        sysUiController.setSystemBarsColor(color = colors.background)
        sysUiController.setNavigationBarColor(color = colors.primary)
    }

    ProvideKafkaColors(colors) {
        MaterialTheme(
            colors = debugColors(darkTheme, KafkaColors.surface),
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

object KafkaTheme {
    @Composable
    val colors: KafkaColorPalette
        get() = KafkaColorAmbient.current
}

/**
 * Kafka custom Color Palette
 */
@Stable
class KafkaColorPalette(
    primary: Color,
    secondary: Color,
    background: Color,
    surface: Color,
    textPrimary: Color,
    textSecondary: Color,
    iconPrimary: Color,
    iconSecondary: Color,
    isDark: Boolean
) {
    var primary by mutableStateOf(primary)
        private set
    var surface by mutableStateOf(surface)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var background by mutableStateOf(background)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var iconPrimary by mutableStateOf(iconPrimary)
        private set
    var iconSecondary by mutableStateOf(iconSecondary)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: KafkaColorPalette) {
        primary = other.primary
        surface = other.surface
        secondary = other.secondary
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        iconPrimary = other.iconPrimary
        iconSecondary = other.iconSecondary
        isDark = other.isDark
    }
}

@Composable
fun ProvideKafkaColors(
    colors: KafkaColorPalette,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors }
    colorPalette.update(colors)
    Providers(KafkaColorAmbient provides colorPalette, children = content)
}

private val KafkaColorAmbient = staticAmbientOf<KafkaColorPalette> {
    error("No KafkaColorPalette provided")
}

/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colors] in preference to [KafkaColors].
 */
fun debugColors(
    darkTheme: Boolean,
    debugColor: Color = Color.Magenta
) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = debugColor,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = !darkTheme
)
