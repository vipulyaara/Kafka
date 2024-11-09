package kafka.reader.core.models

import kafka.reader.core.models.enums.TextStyle

// Extension functions for ContentElement.Text
fun ContentElement.Text.hasStyle(style: TextStyle): Boolean = styles.contains(style)

fun ContentElement.Text.getEffectiveStyle(): TextStyle {
    return when {
        styles.contains(TextStyle.Heading1) -> TextStyle.Heading1
        styles.contains(TextStyle.Heading2) -> TextStyle.Heading2
        styles.contains(TextStyle.Heading3) -> TextStyle.Heading3
        styles.contains(TextStyle.Heading4) -> TextStyle.Heading4
        styles.contains(TextStyle.Heading5) -> TextStyle.Heading5
        styles.contains(TextStyle.Heading6) -> TextStyle.Heading6
        styles.contains(TextStyle.Bold) -> TextStyle.Bold
        styles.contains(TextStyle.Italic) -> TextStyle.Italic
        styles.contains(TextStyle.Monospace) -> TextStyle.Monospace
        styles.contains(TextStyle.Small) -> TextStyle.Small
        else -> TextStyle.Normal
    }
}

fun ContentElement.Text.getDecorationStyles(): Set<TextStyle> = styles.filter { style ->
    when (style) {
        TextStyle.Underline,
        TextStyle.Strikethrough,
        TextStyle.Subscript,
        TextStyle.Superscript,
        TextStyle.SmallCaps -> true

        else -> false
    }
}.toSet() 