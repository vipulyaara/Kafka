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
            "span" -> handleSpan(element, inlineElements, stringBuilder)
            "p" -> handleParagraph(element, inlineElements, stringBuilder)
            "a" -> handleLink(element, inlineElements, stringBuilder)
            "strong", "b" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Bold))
            "em", "i" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Italic))
            "u" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Underline))
            "s", "strike", "del" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Strikethrough))
            "sub" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Subscript))
            "sup" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Superscript))
            "code" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Monospace))
            "small" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Small))
            "mark" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Highlight))
            "q" -> handleQuote(element, inlineElements, stringBuilder)
            "cite" -> handleStyle(element, inlineElements, stringBuilder, setOf(TextStyle.Italic))
            "abbr" -> handleAbbreviation(element, inlineElements, stringBuilder)
            "time" -> handleTime(element, inlineElements, stringBuilder)
            "br" -> handleLineBreak(stringBuilder)
            "wbr" -> {}
            "bdi", "bdo" -> handleBiDirectional(element, inlineElements, stringBuilder)
            "ruby", "rt", "rp" -> handleRuby(element, inlineElements, stringBuilder)
            "data" -> handleData(element, inlineElements, stringBuilder)
            else -> handleCustomElement(element, inlineElements, stringBuilder)
        }
    }

    private fun handleParagraph(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)
        inlineElements.addAll(nestedInline)
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

    private fun handleSpan(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        val start = stringBuilder.length
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)
        
        // Add any styles from the span's classes or attributes
        val styles = StylePropertiesParser.parseStyles(element)
        if (styles.isNotEmpty()) {
            inlineElements.add(InlineElement.Style(start, stringBuilder.length, styles))
        }
        
        inlineElements.addAll(nestedInline)
    }

    private fun handleQuote(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        val start = stringBuilder.length
        stringBuilder.append("")
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)
        stringBuilder.append("") // Closing quote
        inlineElements.addAll(nestedInline)
    }

    private fun handleAbbreviation(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        val start = stringBuilder.length
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)
        
        val title = element.attr("title")
        if (title.isNotEmpty()) {
            inlineElements.add(
                InlineElement.Tooltip(
                    start = start,
                    end = stringBuilder.length,
                    tooltip = title
                )
            )
        }
        inlineElements.addAll(nestedInline)
    }

    private fun handleTime(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)
        inlineElements.addAll(nestedInline)
    }

    private fun handleLineBreak(stringBuilder: StringBuilder) {
        stringBuilder.append('\n')
    }

    private fun handleBiDirectional(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        val start = stringBuilder.length
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)
        
        val dir = element.attr("dir")
        if (dir.isNotEmpty()) {
            inlineElements.add(
                InlineElement.Direction(
                    start = start,
                    end = stringBuilder.length,
                    direction = dir
                )
            )
        }
        inlineElements.addAll(nestedInline)
    }

    private fun handleRuby(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        // For basic support, just parse the base text
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)
        inlineElements.addAll(nestedInline)
    }

    private fun handleData(
        element: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        val start = stringBuilder.length
        val (content, nestedInline) = parse(element)
        appendWithSpacing(stringBuilder, content)
        
        val value = element.attr("value")
        if (value.isNotEmpty()) {
            inlineElements.add(
                InlineElement.Data(
                    start = start,
                    end = stringBuilder.length,
                    value = value
                )
            )
        }
        inlineElements.addAll(nestedInline)
    }

    private fun appendWithSpacing(stringBuilder: StringBuilder, text: String) {
        if (stringBuilder.isNotEmpty() && 
            !stringBuilder.last().isWhitespace() && 
            text.firstOrNull()?.isWhitespace() == false
        ) {
            stringBuilder.append(' ')
        }
        stringBuilder.append(text)
    }
} 