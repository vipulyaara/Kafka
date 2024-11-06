package kafka.reader.core.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface ContentElement {
    @Serializable
    data class Text(
        val content: String,
        val styles: Set<TextStyle> = emptySet(),
        val alignment: TextAlignment = TextAlignment.JUSTIFY,
        val sizeFactor: Float = 1.0f,
        val color: String? = null,
        val backgroundColor: String? = null,
        val letterSpacing: Float? = null,
        val lineHeight: Float? = null,
        val inlineElements: List<InlineElement> = emptyList()
    ) : ContentElement {
        val style: TextStyle
            get() = styles.first()
    }

    @Serializable
    data class Heading(
        val content: String,
        val level: Int
    ) : ContentElement

    @Serializable
    data class Image(
        val path: String,
        val caption: String?,
        val data: ByteArray?,
        val aspectRatio: Float
    ) : ContentElement

    @Serializable
    data class Quote(
        val content: String,
        val attribution: String? = null
    ) : ContentElement

    @Serializable
    data class Listing(
        val items: List<String>,
        val ordered: Boolean,
        val startIndex: Int = 1
    ) : ContentElement

    @Serializable
    data class Table(
        val caption: String? = null,
        val summary: String? = null,
        val columnAlignments: List<ColumnAlignment> = emptyList(),
        val isHeaderRow: Boolean = true,
        val isHeaderColumn: Boolean = false,
        val style: TableStyle = TableStyle.Default,
        val headerElements: List<Text> = emptyList(),
        val rowElements: List<List<Text>> = emptyList()
    ) : ContentElement

    @Serializable
    data class CodeBlock(
        val content: String,
        val language: String? = null
    ) : ContentElement

    @Serializable
    data object Divider : ContentElement
}

enum class TextStyle {
    Normal,
    Bold,
    Italic,
    Underline,
    Strikethrough,
    Subscript,
    Superscript,
    SmallCaps,
    Heading1,
    Heading2,
    Heading3,
    Heading4,
    Heading5,
    Heading6,
    Monospace
}

enum class ColumnAlignment {
    LEFT,
    CENTER,
    RIGHT
}

enum class TableStyle {
    Default,
    Striped,
    Bordered,
    Compact,
    Custom
}

enum class TextAlignment {
    LEFT,
    CENTER,
    RIGHT,
    JUSTIFY
}

@Serializable
sealed interface InlineElement {
    val start: Int
    val end: Int

    @Serializable
    data class Link(
        override val start: Int,
        override val end: Int,
        val href: String
    ) : InlineElement

    @Serializable
    data class Style(
        override val start: Int,
        override val end: Int,
        val styles: Set<TextStyle>
    ) : InlineElement

    @Serializable
    data class Color(
        override val start: Int,
        override val end: Int,
        val color: String
    ) : InlineElement

    @Serializable
    data class BackgroundColor(
        override val start: Int,
        override val end: Int,
        val color: String
    ) : InlineElement
}

fun ColumnAlignment.toTextAlignment() = when(this) {
    ColumnAlignment.LEFT -> TextAlignment.LEFT
    ColumnAlignment.CENTER -> TextAlignment.CENTER
    ColumnAlignment.RIGHT -> TextAlignment.RIGHT
}
