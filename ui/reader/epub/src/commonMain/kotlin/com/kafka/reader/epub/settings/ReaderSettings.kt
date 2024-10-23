package com.kafka.reader.epub.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class ReaderSettings(
    val fontStyle: FontStyle,
    val fontSize: FontSize,
    val background: Background
) {
    data class Background(val color: Color) {
        companion object {
            val Default
                @Composable get() = Background(MaterialTheme.colorScheme.surface)

            @Composable
            fun Options(isDark: Boolean) = if (isDark) {
                listOf(
                    Background(MaterialTheme.colorScheme.surface),
                    Background(MaterialTheme.colorScheme.surfaceVariant),
                    Background(Color(0xFF000000)),
                    Background(Color(0xFF2C2C2E)),
                    Background(Color(0xFF1C1C1E)),
                    Background(Color(0xFF121212)),
                    Background(Color(0xFF3B3B2E)),
                )
            } else {
                listOf(
                    Background(MaterialTheme.colorScheme.surface),
                    Background(MaterialTheme.colorScheme.surfaceVariant),
                    Background(Color(0xFFFFFFFF)),
                    Background(Color(0xFFF5F5F5)),
                    Background(Color(0xFFFAF3E0)),
                    Background(Color(0xFFE9E9E9)),
                    Background(Color(0xFFFDF1F1)),
                )
            }
        }
    }

    data class FontSize(val fontSize: TextUnit, val lineHeight: TextUnit) {
        companion object {
            val Default = Options[2]
            val Options
                get() = listOf(
                    FontSize(12.sp, 18.sp),
                    FontSize(14.sp, 18.sp),
                    FontSize(16.sp, 24.sp),
                    FontSize(18.sp, 26.sp),
                    FontSize(20.sp, 28.sp),
                    FontSize(22.sp, 30.sp),
                    FontSize(24.sp, 32.sp),
                )
        }
    }

    data class FontStyle(val name: String, val fontFamily: FontFamily) {
        companion object {
            val Default = Options[0]

            val Options
                get() = listOf(
                    FontStyle("Serif", FontFamily.Serif),
                    FontStyle("Sans serif", FontFamily.SansSerif),
                    FontStyle("Monospace", FontFamily.Monospace),
                    FontStyle("Cursive", FontFamily.Cursive),
                )
        }
    }

    companion object {
        val Default
            @Composable get() = ReaderSettings(
                FontStyle.Default,
                FontSize.Default,
                Background.Default
            )
    }
}
