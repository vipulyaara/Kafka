package ui.common.theme.theme

import android.os.Build
import android.os.Build.VERSION
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val DefaultSpecs = Specs()

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
        systemUiController.setSystemBarsColor(color = colorScheme.background, darkIcons = isLight)
        systemUiController.setNavigationBarColor(color = colorScheme.surface, darkIcons = isLight)
    }

    MaterialTheme(colorScheme = colorScheme, typography = TypographyEnglish) {
        ProvideRipple {
            content()
        }
    }
}

@Composable
fun ProvideRipple(
    color: Color = MaterialTheme.colorScheme.ripple,
    isBounded: Boolean = true,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalIndication provides rememberRipple(color = color, bounded = isBounded),
        content = content
    )
}

@Composable
fun DisableRipple(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalRippleTheme provides ClearRippleTheme) {
        content()
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isAtLeastS(): Boolean {
    return VERSION.SDK_INT >= 31
}

object ClearRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color = Color.Transparent

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        draggedAlpha = 0.0f,
        focusedAlpha = 0.0f,
        hoveredAlpha = 0.0f,
        pressedAlpha = 0.0f,
    )
}
