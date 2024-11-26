package kafka.reader.core.parser

import com.fleeksoft.ksoup.nodes.Element
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.InlineElement
import kafka.reader.core.models.enums.TextStyle

object ElementStyleParser {
    fun parseTextWithStyle(
        text: String,
        element: Element?,
        inlineElements: List<InlineElement> = emptyList(),
        elementPath: List<Int>
    ): ContentElement.Text {
        if (element == null) return ContentElement.Text(
            content = text,
            inlineElements = inlineElements,
            elementPath = elementPath
        )

        return when {
            element.isHeadingStyle() -> createHeadingText(text, element, elementPath)
            else -> createStyledText(text, element, inlineElements, elementPath)
        }
    }

    private fun Element.isHeadingStyle(): Boolean =
        hasClass("h1") || hasClass("h2") || hasClass("h3") ||
                hasClass("h4") || hasClass("h5") || hasClass("h6") ||
                tagName().lowercase().matches(Regex("h[1-6]|title"))

    private fun createHeadingText(
        text: String,
        element: Element,
        elementPath: List<Int>
    ): ContentElement.Text {
        val (style, sizeFactor) = when {
            element.hasClass("h1") || element.tagName().lowercase() == "h1" ->
                TextStyle.Heading1 to 2.0f

            element.hasClass("h2") || element.tagName().lowercase() == "h2" ->
                TextStyle.Heading2 to 1.8f

            element.hasClass("h3") || element.tagName().lowercase() == "h3" ->
                TextStyle.Heading3 to 1.6f

            element.hasClass("h4") || element.tagName().lowercase() == "h4" ->
                TextStyle.Heading4 to 1.4f

            element.hasClass("h5") || element.tagName().lowercase() == "h5" ->
                TextStyle.Heading5 to 1.2f

            element.hasClass("h6") || element.tagName().lowercase() == "h6" ->
                TextStyle.Heading6 to 1.1f

            else -> TextStyle.Normal to 1.0f
        }

        return ContentElement.Text(
            content = text,
            styles = setOf(style),
            sizeFactor = sizeFactor,
            elementPath = elementPath
        )
    }

    private fun createStyledText(
        text: String,
        element: Element,
        inlineElements: List<InlineElement>,
        elementPath: List<Int>
    ): ContentElement.Text {
        val styleProps = StylePropertiesParser.parse(element)
        return ContentElement.Text(
            content = text,
            styles = styleProps.styles,
            alignment = styleProps.alignment,
            sizeFactor = styleProps.sizeFactor,
            color = styleProps.color,
            backgroundColor = styleProps.backgroundColor,
            letterSpacing = styleProps.letterSpacing,
            lineHeight = styleProps.lineHeight,
            inlineElements = inlineElements,
            indentSize = styleProps.indentSize,
            elementPath = elementPath
        )
    }
}
