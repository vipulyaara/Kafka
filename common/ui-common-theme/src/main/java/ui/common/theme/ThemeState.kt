package ui.common.theme

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class DarkModePreference { ON, OFF, AUTO }
enum class DynamicColorPreference {
    ON, OFF;

    companion object {
        fun from(boolean: Boolean) = if (boolean) ON else OFF
    }
}

enum class DynamicThemePreference { ON, OFF }
enum class ColorPalettePreference { Default, Asphalt, Black, Black_Yellow, Orange }

/**
 * This should be located in app module, but for some ungodly reason kotlinx-serialization plugin isn't working for app module.
 */
@Serializable
data class ThemeState(
    @SerialName("darkMode")
    var darkModePreference: DarkModePreference = DarkModePreference.AUTO,
    @SerialName("dynamic")
    var dynamicThemePreference: DynamicThemePreference = DynamicThemePreference.ON,
    @SerialName("colorPalette")
    var colorPalettePreference: ColorPalettePreference = ColorPalettePreference.Default,
    @SerialName("dynamicColor")
    var dynamicColorPreference: DynamicColorPreference = DynamicColorPreference.ON
) {
    val isDarkMode get() = darkModePreference == DarkModePreference.ON
    val isDynamicColorEnabled get() = dynamicColorPreference == DynamicColorPreference.ON
}
