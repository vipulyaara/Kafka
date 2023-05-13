package ui.common.theme.theme

import android.os.Build
import android.os.Build.VERSION
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppTheme(
    dynamicColor: Boolean = isAtLeastS(),
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && isDarkTheme -> if (isAtLeastS()) {
            dynamicDarkColorScheme(LocalContext.current)
                .copy(background = Color.Black, surface = Color.Black)
        } else {
            DarkAppColors
        }

        dynamicColor && !isDarkTheme -> if (isAtLeastS()) {
            dynamicLightColorScheme(LocalContext.current)
                .copy(background = Color.White, surface = Color.White)
        } else {
            LightAppColors
        }

        isDarkTheme -> DarkAppColors
        else -> LightAppColors
    }

    val systemUiController = rememberSystemUiController()
    val isLight = isSystemInLightTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = isLight)
        systemUiController.setNavigationBarColor(color = Color.Transparent, darkIcons = isLight)
    }

    MaterialTheme(colorScheme = colorScheme, typography = KafkaTypography, content = content)
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
            fontFamily = Inter,
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
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            lineHeight = 18.sp,
        ),
        bodyLarge = default.bodyLarge.copy(fontFamily = DefaultFont),
        bodyMedium = default.bodyMedium,
        bodySmall = default.bodySmall,
        labelLarge = default.labelLarge.copy(fontFamily = DefaultFont),
        labelMedium = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            lineHeight = 15.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 14.sp,
        ),
    )
}

@Composable
fun ProvideRipple(
    isBounded: Boolean = true,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalIndication provides rememberRipple(bounded = isBounded),
        content = content
    )
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isAtLeastS(): Boolean {
    return VERSION.SDK_INT >= 31
}

enum class Theme { Dark, Light, Auto }
