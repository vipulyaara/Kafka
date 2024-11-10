package kafka.reader.core.parser

import TableAnalyzer
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.InlineElement
import kafka.reader.core.models.enums.ColumnAlignment
import kafka.reader.core.models.enums.TableStyle
import kafka.reader.core.models.enums.TextAlignment
import kafka.reader.core.models.enums.TextStyle

class DocumentContentParser(
    private val imageParser: ((Node) -> ContentElement.Image)? = null
) {
    fun parse(node: Node): List<ContentElement> = when (node) {
        is TextNode -> parseTextNode(node)
        is Element -> parseElement(node)
        else -> emptyList()
    }

    private fun parseTextNode(node: TextNode): List<ContentElement> {
        val text = node.text().trim()
        return if (text.isNotEmpty()) {
            listOf(ElementStyleParser.parseTextWithStyle(text, node.parent() as? Element))
        } else emptyList()
    }

    private fun parseElement(element: Element): List<ContentElement> = when {
        element.isStructuralElement() -> parseStructuralElement(element)
        element.isHeadingElement() -> parseHeadingElement(element)
        element.isParagraphElement() -> parseParagraphElement(element)
        element.isListElement() -> parseListElement(element)
        element.isTableElement() -> parseTableElement(element)
        element.isImageElement() -> parseImageElement(element)
        element.isQuoteElement() -> parseQuoteElement(element)
        element.isCodeElement() -> parseCodeElement(element)
        element.isDividerElement() -> parseDividerElement()
        element.isFootnoteElement() -> parseFootnoteElement(element)
        element.isAnnotationElement() -> parseAnnotationElement(element)
        else -> parseDefaultElement(element)
    }

    private fun Element.isStructuralElement() = tagName().lowercase() in setOf(
        "body", "section", "header", "div", "hgroup", "article", "main", "aside"
    )

    private fun Element.isHeadingElement() =
        tagName().lowercase().matches(Regex("h[1-6]"))

    private fun Element.isParagraphElement() =
        tagName().lowercase() == "p"

    private fun Element.isListElement() =
        tagName().lowercase() in setOf("ul", "ol", "dl")

    private fun Element.isTableElement() =
        tagName().lowercase() == "table"

    private fun Element.isImageElement() =
        tagName().lowercase() in setOf("img", "image", "figure")

    private fun Element.isQuoteElement() =
        tagName().lowercase() in setOf("blockquote", "q")

    private fun Element.isCodeElement() =
        tagName().lowercase() in setOf("pre", "code")

    private fun Element.isDividerElement() =
        tagName().lowercase() == "hr"

    private fun Element.isFootnoteElement() =
        hasClass("footnote") || attr("epub:type") == "footnote"

    private fun Element.isAnnotationElement() =
        hasClass("annotation") || attr("epub:type") == "annotation"

    private fun parseStructuralElement(element: Element): List<ContentElement> =
        element.childNodes().flatMap { parse(it) }

    private fun parseHeadingElement(element: Element): List<ContentElement> {
        val level = element.tagName().lowercase().let { tag ->
            tag.removePrefix("h").toIntOrNull() ?: 1
        }
        val (content, _) = InlineContentParser.parse(element)
        return if (content.isNotEmpty()) {
            listOf(ContentElement.Heading(content, level))
        } else emptyList()
    }

    private fun parseParagraphElement(element: Element): List<ContentElement> {
        val (content, inlineElements) = InlineContentParser.parse(element)
        return if (content.isNotEmpty()) {
            listOf(ElementStyleParser.parseTextWithStyle(content, element, inlineElements))
        } else emptyList()
    }

    private fun parseListElement(element: Element): List<ContentElement> {
        val items = when (element.tagName().lowercase()) {
            "dl" -> parseDlItems(element)
            else -> parseListItems(element)
        }

        return if (items.isNotEmpty()) {
            listOf(
                ContentElement.Listing(
                    items = items,
                    ordered = element.tagName().lowercase() == "ol",
                    startIndex = element.attr("start").toIntOrNull() ?: 1
                )
            )
        } else emptyList()
    }

    private fun parseListItems(element: Element): List<String> =
        element.select("li").map { li ->
            InlineContentParser.parse(li).first
        }.filter { it.isNotEmpty() }

    private fun parseDlItems(element: Element): List<String> =
        element.children().flatMap { child ->
            when (child.tagName().lowercase()) {
                "dt" -> listOf(InlineContentParser.parse(child).first)
                "dd" -> listOf("    " + InlineContentParser.parse(child).first)
                else -> emptyList()
            }
        }

    private fun parseTableElement(element: Element): List<ContentElement> {
        val caption = element.selectFirst("caption")?.let { InlineContentParser.parse(it).first }
        val summary = element.attr("summary").takeIf { it.isNotBlank() }

        val tableMetadata = TableAnalyzer.analyzeTable(element)

        val headerRow = element.selectFirst("thead")?.selectFirst("tr")
        val headerElements = headerRow?.select("th, td")?.map { cell ->
            ElementStyleParser.parseTextWithStyle(
                InlineContentParser.parse(cell).first,
                cell
            )
        } ?: emptyList()

        val rows = element.select("tbody tr, tr:not(thead tr)")
        val rowElements = rows.map { row ->
            row.select("td, th").map { cell ->
                ElementStyleParser.parseTextWithStyle(
                    InlineContentParser.parse(cell).first,
                    cell
                )
            }
        }

        return listOf(
            ContentElement.Table(
                caption = caption,
                summary = summary,
                columnAlignments = headerElements.map { cell ->
                    when (cell.alignment) {
                        TextAlignment.LEFT -> ColumnAlignment.LEFT
                        TextAlignment.CENTER -> ColumnAlignment.CENTER
                        TextAlignment.RIGHT -> ColumnAlignment.RIGHT
                        else -> ColumnAlignment.LEFT
                    }
                },
                isHeaderRow = tableMetadata.hasHeader,
                isHeaderColumn = tableMetadata.isDialogueTable,
                headerElements = headerElements,
                rowElements = rowElements,
                columnWeights = tableMetadata.columnWeights,
                columnTypes = tableMetadata.columnTypes.map { it.name },
                style = when {
                    element.hasClass("bordered") -> TableStyle.Bordered
                    element.hasClass("striped") -> TableStyle.Striped
                    element.hasClass("borderless") -> TableStyle.Borderless
                    else -> TableStyle.Borderless
                }
            )
        )
    }

    private fun parseImageElement(element: Element): List<ContentElement> {
        if (imageParser == null) return emptyList()

        val image = imageParser.invoke(element)
        val figure = element.tagName().lowercase() == "figure"

        return if (figure) {
            val caption = element.selectFirst("figcaption")?.let {
                InlineContentParser.parse(it).first
            }
            listOf(image.copy(caption = caption))
        } else {
            listOf(image)
        }
    }

    private fun parseQuoteElement(element: Element): List<ContentElement> {
        // Get all content except the citation
        val mainContent = element.children()
            .filterNot { it.tagName().lowercase() == "cite" }
            .joinToString("\n") { child ->
                InlineContentParser.parse(child).first
            }
            .trim()

        val attribution = element.selectFirst("cite")?.let {
            InlineContentParser.parse(it).first
        }

        return if (mainContent.isNotEmpty()) {
            listOf(
                ContentElement.Quote(
                    content = mainContent,
                    attribution = attribution
                )
            )
        } else emptyList()
    }

    private fun parseCodeElement(element: Element): List<ContentElement> {
        return listOf(ContentElement.CodeBlock(
            content = element.text(),
            language = element.attr("class")
                .split(" ")
                .firstOrNull { it.startsWith("language-") }
                ?.removePrefix("language-")
        ))
    }

    private fun parseDividerElement(): List<ContentElement> = listOf(ContentElement.Divider)

    private fun parseFootnoteElement(element: Element): List<ContentElement> {
        val (content, inlineElements) = InlineContentParser.parse(element)
        return if (content.isNotEmpty()) {
            listOf(
                ElementStyleParser.parseTextWithStyle(
                    content,
                    element,
                    inlineElements + InlineElement.Style(
                        start = 0,
                        end = content.length,
                        styles = setOf(TextStyle.SmallCaps)
                    )
                )
            )
        } else emptyList()
    }

    private fun parseAnnotationElement(element: Element): List<ContentElement> {
        val (content, inlineElements) = InlineContentParser.parse(element)
        return if (content.isNotEmpty()) {
            listOf(
                ElementStyleParser.parseTextWithStyle(
                    content,
                    element,
                    inlineElements + InlineElement.Style(0, content.length, setOf(TextStyle.Italic))
                )
            )
        } else emptyList()
    }

    private fun parseDefaultElement(element: Element): List<ContentElement> {
        val (content, inlineElements) = InlineContentParser.parse(element)
        return if (content.isNotEmpty()) {
            listOf(ElementStyleParser.parseTextWithStyle(content, element, inlineElements))
        } else emptyList()
    }
}
