package kafka.reader.core.models

import kafka.reader.core.models.enums.ColumnAlignment
import kafka.reader.core.models.enums.TableStyle
import kafka.reader.core.models.enums.TextAlignment
import kafka.reader.core.models.enums.TextStyle
import kotlinx.serialization.Serializable

@Serializable
sealed interface ContentElement {
    val elementPath: List<Int>

    @Serializable
    data class Text(
        val content: String,
        override val elementPath: List<Int>,
        val styles: Set<TextStyle> = emptySet(),
        val alignment: TextAlignment = TextAlignment.JUSTIFY,
        val sizeFactor: Float = 1.0f,
        val color: String? = null,
        val backgroundColor: String? = null,
        val letterSpacing: Float? = null,
        val lineHeight: Float? = null,
        val inlineElements: List<InlineElement> = emptyList(),
        val indentSize: Float? = null
    ) : ContentElement

    @Serializable
    data class Heading(
        val content: String,
        val level: Int,
        override val elementPath: List<Int>
    ) : ContentElement

    @Serializable
    data class Image(
        val path: String,
        val caption: String?,
        val data: ByteArray?,
        val aspectRatio: Float,
        override val elementPath: List<Int>
    ) : ContentElement

    @Serializable
    data class Quote(
        val content: String,
        val attribution: String? = null,
        override val elementPath: List<Int>
    ) : ContentElement

    @Serializable
    data class Listing(
        val items: List<String>,
        val ordered: Boolean,
        val startIndex: Int = 1,
        override val elementPath: List<Int>
    ) : ContentElement

    @Serializable
    data class Table(
        val caption: String? = null,
        val summary: String? = null,
        val columnAlignments: List<ColumnAlignment> = emptyList(),
        val isHeaderRow: Boolean = false,
        val isHeaderColumn: Boolean = false,
        val headerElements: List<Text> = emptyList(),
        val rowElements: List<List<Text>> = emptyList(),
        val columnWeights: List<Float> = emptyList(),
        val columnTypes: List<String> = emptyList(),
        val style: TableStyle = TableStyle.Compact,
        override val elementPath: List<Int>
    ) : ContentElement

    @Serializable
    data class CodeBlock(
        val content: String,
        val language: String? = null,
        override val elementPath: List<Int>
    ) : ContentElement

    @Serializable
    data class Divider(override val elementPath: List<Int>) : ContentElement
}
