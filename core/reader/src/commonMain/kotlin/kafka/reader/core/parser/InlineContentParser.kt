package kafka.reader.core.parser

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.TextNode
import kafka.reader.core.models.InlineElement
import kafka.reader.core.models.enums.TextStyle

/**
 * Parser for handling inline content within EPUB elements.
 * Processes text nodes and inline elements like links, styling, and colors.
 */
object InlineContentParser {
    fun parse(element: Element): Pair<String, List<InlineElement>> {
        val inlineElements = mutableListOf<InlineElement>()
        val stringBuilder = StringBuilder()

        element.childNodes().forEach { node ->
            when (node) {
                is TextNode -> handleTextNode(node, stringBuilder)
                is Element -> handleElement(node, inlineElements, stringBuilder)
            }
        }

        return stringBuilder.toString().trim() to inlineElements
    }

    private fun handleTextNode(node: TextNode, stringBuilder: StringBuilder) {
        val text = node.text()
        if (text.isNotBlank()) {
            appendWithSpacing(stringBuilder, text.trim())
        }
    }

    private fun handleElement(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        when (element.tagName().lowercase()) {
            "a" -> handleLink(element, inlineElements, stringBuilder)
            "strong", "b" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Bold))
            "em", "i" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Italic))
            "u" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Underline))
            "s", "strike", "del" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Strikethrough))
            "sub" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Subscript))
            "sup" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Superscript))
            "code" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Monospace))
            "small" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Small))
            else -> handleCustomElement(element, inlineElements, stringBuilder)
        }
    }

    private fun handleCustomElement(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        // Handle elements with style attributes
        val styleProps = StylePropertiesParser.parse(element)
        
        if (styleProps.styles.isNotEmpty()) {
            handleStyle(element, inlineElements, stringBuilder, styleProps.styles)
        }
        if (styleProps.color != null) {
            handleColor(element, inlineElements, stringBuilder, styleProps.color)
        }
        if (styleProps.backgroundColor != null) {
            handleBackgroundColor(element, inlineElements, stringBuilder, styleProps.backgroundColor)
        }
    }

    private fun handleLink(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        val startIndex = stringBuilder.length
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)

        inlineElements.addAll(nestedInline)
        inlineElements.add(
            InlineElement.Link(
                start = startIndex,
                end = startIndex + content.length,
                href = element.attr("href")
            )
        )
    }

    private fun handleStyle(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder,
        styles: Set<TextStyle>
    ) {
        val startIndex = stringBuilder.length
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)

        inlineElements.addAll(nestedInline)
        inlineElements.add(
            InlineElement.Style(
                start = startIndex,
                end = startIndex + content.length,
                styles = styles
            )
        )
    }

    private fun handleColor(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder,
        color: String
    ) {
        val startIndex = stringBuilder.length
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)

        inlineElements.addAll(nestedInline)
        inlineElements.add(
            InlineElement.Color(
                start = startIndex,
                end = stringBuilder.length,
                color = color
            )
        )
    }

    private fun handleBackgroundColor(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder,
        color: String
    ) {
        val startIndex = stringBuilder.length
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)

        inlineElements.addAll(nestedInline)
        inlineElements.add(
            InlineElement.BackgroundColor(
                start = startIndex,
                end = stringBuilder.length,
                color = color
            )
        )
    }

    private fun appendWithSpacing(stringBuilder: StringBuilder, text: String) {
        if (stringBuilder.isNotEmpty() && 
            !stringBuilder.last().isWhitespace() && 
            !text.first().isWhitespace()
        ) {
            stringBuilder.append(' ')
        }
        stringBuilder.append(text)
    }
} 