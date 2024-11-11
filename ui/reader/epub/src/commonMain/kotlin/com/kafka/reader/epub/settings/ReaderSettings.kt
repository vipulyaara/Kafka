package com.kafka.reader.epub.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kafka.reader.epub.settings.ReaderSettings.FontStyleKey
import kotlinx.serialization.Serializable
import ui.common.theme.theme.Inter
import ui.common.theme.theme.Laila

@Serializable
data class ReaderSettings(
    val themeKey: ThemeKey,
    val fontStyleKey: FontStyleKey,
    val fontScale: Float,
    val lineHeightType: LineHeight,
    val textAlignment: TextAlignment,
    val marginScale: Float,
    val horizontalNavigation: Boolean
) {
    val fontSize get() = (DEFAULT_FONT_SIZE * fontScale).sp
    val lineHeight get() = (fontSize.value * lineHeightType.multiplier).sp
    val horizontalMargin get() = (DEFAULT_MARGIN * marginScale).dp

    enum class FontStyleKey {
        Serif, SansSerif, Cursive, Laila;

        companion object {
            fun default(language: String) = if (language == "hi") {
                Laila
            } else {
                SansSerif
            }
        }
    }

    enum class ThemeKey {
        System, Light, Dark, Sepia, Night;

        companion object {
            val Default = System
        }
    }

    @Serializable
    enum class LineHeight(val multiplier: Float, val label: String) {
        COMPACT(1.4f, "Compact"),
        NORMAL(1.7f, "Normal"),
        RELAXED(2f, "Relaxed");

        companion object {
            val DEFAULT = NORMAL
        }
    }

    @Serializable
    enum class TextAlignment(val label: String) {
        LEFT("Left"),
        RIGHT("Right"),
        JUSTIFY("Justify");

        fun asAlignment() = when (this) {
            LEFT -> TextAlign.Start
            RIGHT -> TextAlign.End
            JUSTIFY -> TextAlign.Justify
        }

        companion object {
            val DEFAULT = LEFT
        }
    }

    companion object {
        const val DEFAULT_LANGUAGE = "en"
        const val DEFAULT_FONT_SIZE = 16f
        const val DEFAULT_MARGIN = 16f
        private const val DEFAULT_MARGIN_SCALE = 1f

        val fontScaleOptions =
            listOf(0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 2f, 2.5f, 3f)
        val marginScaleOptions = listOf(0.25f, 0.5f, DEFAULT_MARGIN_SCALE, 1.5f, 2f, 2.5f, 3f)

        fun default(language: String) = ReaderSettings(
            themeKey = ThemeKey.Default,
            fontStyleKey = FontStyleKey.default(language),
            fontScale = 1.0f,
            lineHeightType = LineHeight.DEFAULT,
            textAlignment = TextAlignment.DEFAULT,
            marginScale = DEFAULT_MARGIN_SCALE,
            horizontalNavigation = false
        )
    }
}

val ReaderSettings.font: ReaderFont
    @Composable get() = ReaderFont.from(fontStyleKey)

val ReaderSettings.theme: ReaderTheme
    @Composable get() = ReaderTheme.from(themeKey)

data class ReaderTheme(
    val key: ReaderSettings.ThemeKey,
    val backgroundColor: Color = Color.Transparent,
    val contentColor: Color = Color.Transparent
) {
    val isSystemTheme get() = key == ReaderSettings.ThemeKey.System

    val prominentColor
        @Composable get() = MaterialTheme.colorScheme.primary

    companion object {
        val options
            @Composable get() = listOf(
                ReaderTheme(
                    key = ReaderSettings.ThemeKey.System,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                ReaderTheme(
                    key = ReaderSettings.ThemeKey.Light,
                    backgroundColor = Color(0xFFFFFFFF),
                    contentColor = Color(0xFF000000)
                ),
                ReaderTheme(
                    key = ReaderSettings.ThemeKey.Dark,
                    backgroundColor = Color(0xFF000000),
                    contentColor = Color(0xFFFFFFFF)
                ),
                ReaderTheme(
                    key = ReaderSettings.ThemeKey.Sepia,
                    backgroundColor = Color(0xFFFAF3E0),
                    contentColor = Color(0xFF000000)
                ),
                ReaderTheme(
                    key = ReaderSettings.ThemeKey.Night,
                    backgroundColor = Color(0xFF2C1810),
                    contentColor = Color(0xFFFFB74D)
                ),
            )

        @Composable
        fun from(key: ReaderSettings.ThemeKey): ReaderTheme {
            return options.first { it.key == key }
        }
    }
}

data class ReaderFont(
    val key: FontStyleKey,
    val name: String,
    val fontFamily: FontFamily,
    val fontWeight: FontWeight,
    val supportedLanguages: Set<String> = setOf("*")
) {
    companion object {
        @Composable
        fun from(value: FontStyleKey): ReaderFont = options().first { it.key == value }

        private fun ReaderFont.supportsLanguage(language: String): Boolean =
            supportedLanguages.contains("*") || supportedLanguages.contains(language.lowercase())

        @Composable
        fun options(language: String) = options().filter { it.supportsLanguage(language) }

        @Composable
        private fun options(): List<ReaderFont> = buildList {
            // Language-specific fonts
            add(ReaderFont(FontStyleKey.Laila, "Laila", Laila, FontWeight.Medium, setOf("hi")))

            // Default fonts for all languages
            add(ReaderFont(FontStyleKey.SansSerif, "Sans Serif", Inter, FontWeight.Normal))
            add(ReaderFont(FontStyleKey.Serif, "Serif", FontFamily.Serif, FontWeight.Normal))
            add(ReaderFont(FontStyleKey.Cursive, "Cursive", FontFamily.Cursive, FontWeight.Normal))
        }
    }
}
