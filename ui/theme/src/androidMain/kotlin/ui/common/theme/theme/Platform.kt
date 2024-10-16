package ui.common.theme.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun colorScheme(
    useDarkColors: Boolean,
    useTrueContrast: Boolean,
): ColorScheme = when {
    isAtLeastS() && useDarkColors -> {
        dynamicDarkColorScheme(LocalContext.current).run {
            if (useTrueContrast) {
                copy(background = Color.Black, surface = Color.Black)
            } else {
                this
            }
        }
    }

    isAtLeastS() && !useDarkColors -> {
        dynamicLightColorScheme(LocalContext.current).run {
            if (useTrueContrast) {
                copy(background = Color.White, surface = Color.White)
            } else {
                this
            }
        }
    }

    useDarkColors -> DarkAppColors
    else -> LightAppColors
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isAtLeastS(): Boolean {
    return Build.VERSION.SDK_INT >= 31
}
