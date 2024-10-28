package ui.common.theme.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun colorScheme(
    useDarkColors: Boolean,
    useTrueContrast: Boolean
): ColorScheme = if (useDarkColors) {
    DarkAppColors
} else {
    LightAppColors
}

actual fun setStatusBarColor(context: Any?, lightStatusBar: Boolean) {
    // todo
}
