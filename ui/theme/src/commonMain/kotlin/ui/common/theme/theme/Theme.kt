package ui.common.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kafka.data.prefs.PreferencesStore
import com.kafka.data.prefs.Theme
import com.kafka.data.prefs.observeTheme

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isTrueContrast: Boolean = true,
    content: @Composable () -> Unit,
) {
    val themeColor = if (isDarkTheme) ThemeColor.Dark else ThemeColor.Light

    CompositionLocalProvider(LocalThemeColor provides themeColor) {
        MaterialTheme(
            colorScheme = colorScheme(isDarkTheme, isTrueContrast),
            typography = KafkaTypography,
            content = content
        )
    }
}

val KafkaTypography: Typography
    @Composable get() {
        val default = Typography()
        return Typography(
            displayLarge = default.displayLarge.copy(fontFamily = DefaultFont),
            displayMedium = default.displayMedium.copy(fontFamily = DefaultFont),
            displaySmall = default.displaySmall.copy(fontFamily = DefaultFont),
            headlineLarge = default.headlineLarge.copy(fontFamily = DefaultFont),
            headlineMedium = default.headlineMedium.copy(fontFamily = DefaultFont),
            headlineSmall = TextStyle(
                fontFamily = DefaultFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
            ),
            titleLarge = default.titleLarge.copy(
                fontFamily = DefaultFont,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            ),
            titleMedium = TextStyle(
                fontFamily = DefaultFont,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
            titleSmall = TextStyle(
                fontFamily = DefaultFont,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                lineHeight = 18.sp,
            ),
            bodyLarge = default.bodyLarge.copy(fontFamily = DefaultFont),
            bodyMedium = default.bodyMedium,
            bodySmall = default.bodySmall,
            labelLarge = default.labelLarge.copy(fontFamily = DefaultFont),
            labelMedium = TextStyle(
                fontFamily = DefaultFont,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                lineHeight = 15.sp,
            ),
            labelSmall = TextStyle(
                fontFamily = DefaultFont,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 14.sp,
            ),
        )
    }

@Composable
fun PreferencesStore.shouldUseDarkColors(): Boolean {
    val themePreference by remember { observeTheme() }.collectAsState(Theme.SYSTEM)

    return themePreference.shouldUseDarkColors()
}

@Composable
fun Theme.shouldUseDarkColors(): Boolean {
    return when (this) {
        Theme.SYSTEM -> isSystemInDarkTheme()
        Theme.DARK -> true
        Theme.LIGHT -> false
        else -> isSystemInDarkTheme()
    }
}

val LocalTheme = staticCompositionLocalOf<Theme> {
    error("LocalTheme not provided")
}

@Composable
fun Theme.isDark() = when (this) {
    Theme.LIGHT -> false
    Theme.DARK -> true
    Theme.SYSTEM -> isSystemInDarkTheme()
}

expect fun setStatusBarColor(context: Any?, lightStatusBar: Boolean)
