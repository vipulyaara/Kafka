package kafka.reader.core.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface ContentElement {
    data class Text(
        val content: String,
        val style: TextStyle = TextStyle.Normal
    ) : ContentElement

    data class Heading(
        val content: String,
        val level: Int
    ) : ContentElement

    data class Image(
        val path: String,
        val caption: String?,
        val data: ByteArray?,
        val aspectRatio: Float
    ) : ContentElement

    data class Quote(
        val content: String,
        val attribution: String? = null
    ) : ContentElement

    data class List(
        val items: kotlin.collections.List<String>,
        val ordered: Boolean,
        val startIndex: Int = 1
    ) : ContentElement

    data class Table(
        val headers: kotlin.collections.List<String>,
        val rows: kotlin.collections.List<kotlin.collections.List<String>>
    ) : ContentElement

    data class CodeBlock(
        val content: String,
        val language: String? = null
    ) : ContentElement

    data class Link(
        val content: String,
        val href: String? = null
    ) : ContentElement

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
    Heading6
} 