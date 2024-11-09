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
} 