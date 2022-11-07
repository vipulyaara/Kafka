package ui.common.theme.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.compositeOver

val brandRed = Color(0xffff006a)
val brandBlue = Color(0xff0067DD)
val brandBlue_2 = Color(0xff24aaff)

val white = Color(0xFFFFFFFF)
val whiteCream = Color(0xFFFAFAFA)
val brandBlue100 = Color(0xffDFE9FF)
val brandBlue200 = Color(0xbbCCD7F0)
val brandBlue600 = Color(0xff7B8994)

val darkGrey600 = Color(0xff212121)
val surfaceLight = Color(0xFFfbfcff)
val darkGrey700 = Color(0xff1A1B1C)
val darkGrey800 = Color(0xff121212)
val darkGrey900 = Color(0xFF0a0b0c)
val darkBackground = Color(0xFF16181D)
val darkSurface = Color(0xFF22242C)
val textSecondaryDark = Color(0xff888888)

val Primary = Color(0xffff006a)
val PrimaryVariant = Color(0xFF221652)
val Secondary = Color(0xff24aaff)
val SecondaryVariant = Color(0xFFef0076)

val WhiteTransparent = Color(0x80FFFFFF)

val Red = Color(0xFFFF3B30)
val Red700 = Color(0xFFC0392b)
val Orange = Color(0xFFFF9500)
val Yellow = Color(0xFFFFCC00)
val Yellow500 = Color(0xFFFBBC04)
val Green = Color(0xFF4CD964)
val Blue300 = Color(0xFF5AC8FA)
val Blue = Color(0xFF007AFF)
val Purple = Color(0xFF5856D6)
val Asphalt = Color(0xFF2c3e50)

val error = Color(0xffBA0550)

val ColorScheme.divider
    @Composable get() = MaterialTheme.colorScheme.outline

val ColorScheme.ripple
    @Composable get() = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)

val ColorScheme.dividerVariant
    @Composable get() = MaterialTheme.colorScheme.surfaceVariant

val ColorScheme.textPrimary
    @Composable get() = if (isSystemInLightTheme()) Color(0xff000000) else whiteCream

val ColorScheme.textSecondary
    @Composable get() = MaterialTheme.colorScheme.secondary

val ColorScheme.iconPrimary
    @Composable get() = if (isSystemInLightTheme()) darkGrey800 else whiteCream

val ColorScheme.yellow
    @Composable get() = Color(0xfff6bf26)

val ColorScheme.brandRed
    @Composable get() = Color(0xffff006a)

val ColorScheme.black
    @Composable get() = Color(0xff000000)

val ColorScheme.white
    @Composable get() = Color(0xffffffff)

val ColorScheme.blue
    @Composable get() = Color(0xff3D84FD)

val ColorScheme.yellowDark
    @Composable get() = Color(0xffCFA224)

val ColorScheme.shadowMaterial
    @Composable get() = if (isSystemInLightTheme()) primary.copy(alpha = 0.5f) else primary.copy(alpha = 0.2f)

@Composable
fun isSystemInLightTheme() = !isSystemInDarkTheme()

fun parseColor(hexColor: String) = Color(android.graphics.Color.parseColor(hexColor))

fun Int.toColor() = Color(this)

internal val DarkAppColors = darkColorScheme(
    primary = brandBlue,
    secondary = textSecondaryDark,
    background = darkGrey800,
    surface = darkSurface,
    tertiary = Color(0xffCFA224),
    error = error,
    secondaryContainer = darkSurface,
    onBackground = whiteCream,
    onSurface = whiteCream,
    onError = white,
    outline = whiteCream,
    onPrimary = white,
    onSecondary = white,
)

internal val LightAppColors = lightColorScheme(
    primary = brandBlue,
    secondary = Asphalt,
    background = whiteCream,
    surface = white,
    tertiary = Color(0xffCFA224),
    tertiaryContainer = brandBlue.copy(alpha = 0.1f),
    secondaryContainer = brandBlue.copy(alpha = 0.1f),
    error = error,
    onPrimary = white,
    onSecondary = white,
    onBackground = darkGrey800,
    onSurface = darkGrey800,
    onError = white,
    outline = darkGrey800
)

@Composable
fun Color.disabledAlpha(condition: Boolean): Color =
    copy(alpha = if (condition) alpha else DefaultAlpha)

@Composable
fun Color.contrastComposite(alpha: Float = 0.1f) =
    contentColorFor(this).copy(alpha = alpha).compositeOver(this)

@Composable
fun translucentSurfaceColor() =
    MaterialTheme.colorScheme.surface.copy(alpha = AppBarAlphas.translucentBarAlpha())

fun Modifier.translucentSurface() = composed { background(translucentSurfaceColor()) }
