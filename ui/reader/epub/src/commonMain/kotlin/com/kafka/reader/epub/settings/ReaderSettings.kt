package com.kafka.reader.epub.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kafka.common.toColor
import com.kafka.common.toHexString
import ui.common.theme.theme.Laila

data class ReaderSettings(
    val fontScale: Float = 1f,
    val lineHeightType: LineHeight = LineHeight.NORMAL,
    val fontStyle: FontStyle = FontStyle.Default,
    val marginScale: Float = 1f,
    val isDarkMode: Boolean = false,
    val background: Background = Background.default(isDarkMode),
    val isReadingMode: Boolean = false,
    val textAlignment: TextAlignment = TextAlignment.LEFT
) {
    val fontSize get() = (16 * fontScale).sp
    val lineHeight get() = (fontSize.value * lineHeightType.multiplier).sp
    val horizontalMargin get() = (16 * marginScale).dp

    val backgroundColor: Color
        @Composable
        get() = when {
            // Reading mode takes precedence - warm dark background
            isReadingMode -> Color(0xFF2C1810) // Warm dark brown color
            else -> background.color
        }

    val textColor: Color
        @Composable
        get() = when {
            isReadingMode -> Color(0xFFFFB74D) // Existing warm orange
            else -> calculateTextColor()
        }

    @Composable
    private fun calculateTextColor(): Color {
        // Now use backgroundColor instead of background.color
        val baseColor = contentColorFor(backgroundColor)
        
        return if (isDarkMode) {
            baseColor.copy(alpha = 0.87f)
        } else {
            baseColor
        }
    }

    enum class LineHeight(val multiplier: Float, val label: String) {
        COMPACT(1.2f, "Compact"),
        NORMAL(1.4f, "Normal"),
        RELAXED(1.7f, "Relaxed");

        companion object {
            fun from(value: String) = LineHeight.entries.find { it.name == value } ?: COMPACT
        }
    }

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
            fun from(value: String) = TextAlignment.entries.find { it.name == value } ?: LEFT
        }
    }

    data class FontStyle(
        val name: String,
        val fontFamily: FontFamily,
        val fontWeight: FontWeight
    ) {
        override fun toString(): String = name

        companion object {
            // todo
            val Default = FontStyle("Default", FontFamily.Default, FontWeight.Normal)

            fun fromString(value: String): FontStyle =
                defaultOptions.find { it.name == value } ?: Default

            @Composable
            fun getOptionsForLanguage(language: String) = when {
                language.startsWith("hi", true) -> hindiOptions
                else -> defaultOptions
            }

            private val defaultOptions = listOf(
                Default,
                FontStyle("Sans Serif", FontFamily.SansSerif, FontWeight.Normal),
                FontStyle("Serif", FontFamily.Serif, FontWeight.Normal),
                FontStyle("Monospace", FontFamily.Monospace, FontWeight.Normal)
            )

            private val hindiOptions
                @Composable get() = listOf(
                    FontStyle("Laila", Laila, FontWeight.Medium),
                    FontStyle("Laila", Laila, FontWeight.Medium),
                    // Add more Hindi-specific fonts
                )
        }
    }

    data class Background(val color: Color) {
        override fun toString(): String = color.toHexString()

        companion object {
            fun default(isDark: Boolean) = if (isDark) {
                Background(Color(0xFF000000))
            } else {
                Background(Color(0xFFFFFFFF))
            }

            fun fromString(value: String): Background = Background(value.toColor())

            @Composable
            fun getOptions(isDark: Boolean) = if (isDark) darkOptions else lightOptions

            private val lightOptions
                @Composable get() = listOf(
                    Background(Color(0xFFFFFFFF)),
                    Background(MaterialTheme.colorScheme.surfaceVariant),
                    Background(Color(0xFFF5F5F5)),
                    Background(Color(0xFFFAF3E0)),
                    Background(Color(0xFFE9E9E9))
                )

            private val darkOptions
                @Composable get() = listOf(
                    Background(Color(0xFF000000)),
                    Background(MaterialTheme.colorScheme.surfaceVariant),
                    Background(Color(0xFF2C2C2E)),
                    Background(Color(0xFF1C1C1E)),
                    Background(Color(0xFF121212))
                )
        }
    }

    companion object {
        val fontScaleOptions = listOf(0.5f, 0.75f, 1f, 1.2f, 1.4f, 1.5f, 1.7f, 2f, 2.5f, 3f)
        val marginScaleOptions = listOf(0.25f, 0.5f, 1f, 1.5f, 2f, 2.5f, 3f)
    }
}
