package kafka.reader.core.models

import kafka.reader.core.models.enums.TextStyle
import kotlinx.serialization.Serializable

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

    data class Tooltip(
        override val start: Int,
        override val end: Int,
        val tooltip: String
    ) : InlineElement
    
    data class Direction(
        override val start: Int,
        override val end: Int,
        val direction: String
    ) : InlineElement
    
    data class Data(
        override val start: Int,
        override val end: Int,
        val value: String
    ) : InlineElement

    data class Highlight(
        val id: String,
        val color: String,
        val note: String?,
        override val start: Int,
        override val end: Int,
    ) : InlineElement
}

fun List<TextHighlight>.toInlineElements(): List<InlineElement> {
    return map { highlight ->
        InlineElement.Highlight(
            id = highlight.id,
            start = highlight.startOffset,
            end = highlight.endOffset,
            color = highlight.color,
            note = highlight.note
        )
    }
}