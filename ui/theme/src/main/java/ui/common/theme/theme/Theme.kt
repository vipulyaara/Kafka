package ui.common.theme.theme

import android.os.Build
import android.os.Build.VERSION
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kafka.data.prefs.PreferencesStore
import com.kafka.data.prefs.Theme
import com.kafka.data.prefs.observeTheme
import com.kafka.data.prefs.observeTrueContrast

@Composable
fun AppTheme(
    dynamicColor: Boolean = isAtLeastS(),
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isTrueContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && isDarkTheme -> if (isAtLeastS()) {
            if (isTrueContrast) {
                dynamicDarkColorScheme(LocalContext.current)
                    .copy(background = Color.Black, surface = Color.Black)
            } else {
                dynamicDarkColorScheme(LocalContext.current)
            }
        } else {
            DarkAppColors
        }

        dynamicColor && !isDarkTheme -> if (isAtLeastS()) {
            if (isTrueContrast) {
                dynamicLightColorScheme(LocalContext.current)
                    .copy(background = Color.White, surface = Color.White)
            } else {
                dynamicLightColorScheme(LocalContext.current)
            }
        } else {
            LightAppColors
        }

        isDarkTheme -> DarkAppColors
        else -> LightAppColors
    }

    val themeColor = if (isDarkTheme) ThemeColor.Dark else ThemeColor.Light

    CompositionLocalProvider(LocalThemeColor provides themeColor) {
        MaterialTheme(colorScheme = colorScheme, typography = KafkaTypography, content = content)
    }
}

val KafkaTypography by lazy {
    val default = Typography()
    Typography(
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

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isAtLeastS(): Boolean {
    return VERSION.SDK_INT >= 31
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

@Composable
fun PreferencesStore.shouldUseTrueContrast(): Boolean {
    val themePreference by remember { observeTrueContrast() }.collectAsState(true)
    return themePreference
}

val LocalTheme = staticCompositionLocalOf<Theme> {
    error("LocalTheme not provided")
}
