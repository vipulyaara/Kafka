package ui.common.theme.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.*
import ui.common.theme.ThemeState

val LocalThemeState = staticCompositionLocalOf<ThemeState> {
    error("No LocalThemeState provided")
}
private val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No LocalAppColors provided")
}
private val LocalSpecs = staticCompositionLocalOf<Specs> {
    error("No LocalSpecs provided")
}

object AppTheme {
    val state: ThemeState
        @Composable
        get() = LocalThemeState.current

    val specs: Specs
        @Composable
        get() = LocalSpecs.current
}

@Composable
fun ProvideAppTheme(
    theme: ThemeState,
    colors: AppColors,
    specs: Specs = DefaultSpecs,
    content: @Composable () -> Unit
) {
    val appColors = remember { colors.copy() }.apply { update(colors) }

    CompositionLocalProvider(
        LocalThemeState provides theme,
        LocalAppColors provides appColors,
        LocalSpecs provides specs,
        content = content
    )
}

@Stable
data class AppColors(
    val _materialColors: ColorScheme
) {
    var materialColors by mutableStateOf(_materialColors)
        private set

    fun update(other: AppColors) {
        materialColors = other.materialColors
    }
}
