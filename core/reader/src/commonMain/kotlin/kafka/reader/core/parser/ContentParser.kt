package kafka.reader.core.parser

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.InlineElement
import kafka.reader.core.models.TextStyle

object ContentParser {

    fun parseNode(
        node: Node,
        imageParser: ((Node) -> ContentElement.Image)? = null
    ): List<ContentElement> {
        return buildList {
            when (node) {
                is TextNode -> {
                    val text = node.text().trim()
                    if (text.isNotEmpty()) {
                        add(parseTextWithStyle(text, node.parent() as? Element))
                    }
                }

                is Element -> {
                    when (node.tagName().lowercase()) {
                        "p" -> {
                            val (content, inlineElements) = parseInlineContent(node)
                            if (content.isNotEmpty()) {
                                add(parseTextWithStyle(content, node, inlineElements))
                            }
                        }

                        "blockquote" -> {
                            val attribution = node.selectFirst("cite")
                                ?.let { parseInlineContent(it).first }
                            add(
                                ContentElement.Quote(
                                    content = parseInlineContent(node).first,
                                    attribution = attribution
                                )
                            )
                        }

                        "img", "image" -> {
                            imageParser?.let { parser -> add(parser(node)) }
                        }

                        "pre", "code" -> {
                            add(ContentElement.CodeBlock(
                                content = node.text(),
                                language = node.attr("class")
                                    .split(" ")
                                    .firstOrNull { it.startsWith("language-") }
                                    ?.removePrefix("language-")
                            ))
                        }

                        "hr" -> add(ContentElement.Divider)
                        "table" -> add(parseTable(node))

                        "div", "section" -> {
                            node.childNodes().forEach { child ->
                                addAll(parseNode(child, imageParser))
                            }
                        }

                        "span" -> {
                            when {
                                node.hasClass("smc") -> {
                                    add(
                                        ContentElement.Text(
                                            content = node.text().trim(),
                                            styles = setOf(TextStyle.SmallCaps)
                                        )
                                    )
                                }

                                else -> {
                                    val (content, inlineElements) = parseInlineContent(node)
                                    if (content.isNotEmpty()) {
                                        add(parseTextWithStyle(content, node, inlineElements))
                                    }
                                }
                            }
                        }

                        else -> {
                            node.childNodes().forEach { child ->
                                addAll(parseNode(child, imageParser))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createTextElement(
        content: String,
        element: Element,
        inlineElements: List<InlineElement> = emptyList()
    ): ContentElement.Text {
        val styleProps = parseStyles(element)
        return ContentElement.Text(
            content = content,
            styles = styleProps.styles,
            alignment = styleProps.alignment,
            sizeFactor = styleProps.sizeFactor,
            color = styleProps.color,
            backgroundColor = styleProps.backgroundColor,
            letterSpacing = styleProps.letterSpacing,
            lineHeight = styleProps.lineHeight,
            inlineElements = inlineElements
        )
    }

    private fun parseTextWithStyle(
        text: String,
        element: Element?,
        inlineElements: List<InlineElement> = emptyList()
    ): ContentElement.Text {
        if (element == null) return ContentElement.Text(text, inlineElements = inlineElements)

        return when (element.tagName().lowercase()) {
            "h1" -> ContentElement.Text(
                content = text,
                styles = setOf(TextStyle.Heading1),
                sizeFactor = 2.0f
            )

            "h2" -> ContentElement.Text(
                content = text,
                styles = setOf(TextStyle.Heading2),
                sizeFactor = 1.75f
            )

            "h3" -> ContentElement.Text(
                content = text,
                styles = setOf(TextStyle.Heading3),
                sizeFactor = 1.5f
            )

            "h4" -> ContentElement.Text(
                content = text,
                styles = setOf(TextStyle.Heading4),
                sizeFactor = 1.25f
            )

            "h5" -> ContentElement.Text(
                content = text,
                styles = setOf(TextStyle.Heading5),
                sizeFactor = 1.1f
            )

            "h6" -> ContentElement.Text(
                content = text,
                styles = setOf(TextStyle.Heading6),
                sizeFactor = 1.0f
            )

            "p" -> createTextElement(text, element, inlineElements)
            "caption" -> ContentElement.Text(
                content = text,
                styles = setOf(TextStyle.Italic)
            )

            "strong" -> ContentElement.Text(
                content = text,
                styles = setOf(TextStyle.Heading3)
            )

            else -> createTextElement(text, element, inlineElements)
        }
    }

    private fun parseInlineContent(element: Element): Pair<String, List<InlineElement>> {
        val inlineElements = mutableListOf<InlineElement>()
        val stringBuilder = StringBuilder()

        element.childNodes().forEach { node ->
            when (node) {
                is TextNode -> {
                    val text = node.text()
                    if (text.isNotBlank()) {
                        if (stringBuilder.isNotEmpty() && !stringBuilder.last()
                                .isWhitespace() && !text.first().isWhitespace()
                        ) {
                            stringBuilder.append(' ')
                        }
                        stringBuilder.append(text.trim())
                    }
                }

                is Element -> {
                    when (node.tagName().lowercase()) {
                        "a" -> handleLinkElement(node, inlineElements, stringBuilder)
                        "strong", "b" -> handleStyledElement(
                            node,
                            inlineElements,
                            stringBuilder,
                            setOf(TextStyle.Bold)
                        )

                        "em", "i" -> handleStyledElement(
                            node,
                            inlineElements,
                            stringBuilder,
                            setOf(TextStyle.Italic)
                        )

                        "u" -> handleStyledElement(
                            node,
                            inlineElements,
                            stringBuilder,
                            setOf(TextStyle.Underline)
                        )

                        "s", "strike", "del" -> handleStyledElement(
                            node,
                            inlineElements,
                            stringBuilder,
                            setOf(TextStyle.Strikethrough)
                        )

                        else -> {
                            // Handle elements with style attributes
                            val styleProps = parseStyles(node)
                            if (styleProps.styles.isNotEmpty()) {
                                handleStyledElement(
                                    node,
                                    inlineElements,
                                    stringBuilder,
                                    styleProps.styles
                                )
                            }
                            if (styleProps.color != null) {
                                handleColorElement(
                                    node,
                                    inlineElements,
                                    stringBuilder,
                                    styleProps.color
                                )
                            }
                            if (styleProps.backgroundColor != null) {
                                handleBackgroundColorElement(
                                    node,
                                    inlineElements,
                                    stringBuilder,
                                    styleProps.backgroundColor
                                )
                            }
                        }
                    }
                }
            }
        }

        return stringBuilder.toString().trim() to inlineElements
    }

    private fun handleLinkElement(
        node: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder
    ) {
        if (stringBuilder.isNotEmpty() && !stringBuilder.last().isWhitespace()) {
            stringBuilder.append(' ')
        }

        val startIndex = stringBuilder.length
        val (content, _) = parseInlineContent(node)
        stringBuilder.append(content)

        if (!content.endsWith(' ')) {
            stringBuilder.append(' ')
        }

        inlineElements.add(
            InlineElement.Link(
                start = startIndex,
                end = startIndex + content.length,
                href = node.attr("href")
            )
        )
    }

    private fun handleStyledElement(
        node: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder,
        styles: Set<TextStyle>
    ) {
        if (stringBuilder.isNotEmpty() && !stringBuilder.last().isWhitespace()) {
            stringBuilder.append(' ')
        }

        val startIndex = stringBuilder.length
        val (content, nestedInline) = parseInlineContent(node)
        stringBuilder.append(content)

        if (!content.endsWith(' ')) {
            stringBuilder.append(' ')
        }

        inlineElements.addAll(nestedInline)
        inlineElements.add(
            InlineElement.Style(
                start = startIndex,
                end = startIndex + content.length,
                styles = styles
            )
        )
    }

    private fun handleColorElement(
        node: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder,
        color: String
    ) {
        val startIndex = stringBuilder.length
        val (content, nestedInline) = parseInlineContent(node)
        stringBuilder.append(content)
        inlineElements.addAll(nestedInline)
        inlineElements.add(
            InlineElement.Color(
                start = startIndex,
                end = stringBuilder.length,
                color = color
            )
        )
    }

    private fun handleBackgroundColorElement(
        node: Element,
        inlineElements: MutableList<InlineElement>,
        stringBuilder: StringBuilder,
        color: String
    ) {
        val startIndex = stringBuilder.length
        val (content, nestedInline) = parseInlineContent(node)
        stringBuilder.append(content)
        inlineElements.addAll(nestedInline)
        inlineElements.add(
            InlineElement.BackgroundColor(
                start = startIndex,
                end = stringBuilder.length,
                color = color
            )
        )
    }
}
