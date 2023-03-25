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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppTheme(
    dynamicColor: Boolean = isAtLeastS(),
    content: @Composable () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    val colorScheme = when {
        dynamicColor && isDarkTheme -> if (isAtLeastS()) {
            dynamicDarkColorScheme(LocalContext.current)
        } else {
            DarkAppColors
        }

        dynamicColor && !isDarkTheme -> if (isAtLeastS()) {
            dynamicLightColorScheme(LocalContext.current)
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

    MaterialTheme(colorScheme = colorScheme, typography = KafkaTypography) {
        content()
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
        headlineSmall = default.headlineSmall.copy(fontFamily = DefaultFont),
        titleLarge = default.titleLarge.copy(
            fontFamily = DefaultFont,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        ),
        titleMedium = default.titleMedium.copy(
            fontFamily = DefaultFont,
            fontWeight = FontWeight.Medium
        ),
        titleSmall = default.titleSmall.copy(
            fontFamily = DefaultFont,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.01.sp
        ),
        bodyLarge = default.bodyLarge.copy(fontFamily = DefaultFont),
        bodyMedium = default.bodyMedium.copy(
            fontFamily = DefaultFont,
            fontWeight = FontWeight.Medium
        ),
        bodySmall = default.bodySmall.copy(
            fontFamily = DefaultFont,
            fontWeight = FontWeight.Medium
        ),
        labelLarge = default.labelLarge.copy(fontFamily = DefaultFont),
        labelMedium = default.labelMedium.copy(
            fontFamily = DefaultFont,
            letterSpacing = 0.1.sp,
            fontWeight = FontWeight.Medium
        ),
        labelSmall = default.labelSmall.copy(fontFamily = DefaultFont),
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
