package ui.common.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

object AppBarAlphas {
    @Composable
    fun translucentBarAlpha(): Float = when {
        isSystemInDarkTheme() -> 0.85f
        else -> 0.92f
    }
}
