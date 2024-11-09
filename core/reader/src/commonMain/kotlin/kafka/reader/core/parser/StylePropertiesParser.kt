package kafka.reader.core.parser

import com.fleeksoft.ksoup.nodes.Element
import kafka.reader.core.models.enums.TextAlignment
import kafka.reader.core.models.enums.TextStyle

internal data class StyleProperties(
    val styles: Set<TextStyle> = setOf(),
    val sizeFactor: Float = 1.0f,
    val color: String? = null,
    val backgroundColor: String? = null,
    val letterSpacing: Float? = null,
    val lineHeight: Float? = null,
    val alignment: TextAlignment = TextAlignment.JUSTIFY,
    val indentSize: Float? = null
)

internal object StylePropertiesParser {
    fun parse(element: Element): StyleProperties {
        return StyleProperties(
            styles = parseStyles(element),
            alignment = parseAlignment(element),
            sizeFactor = parseSizeFactor(element),
            color = parseColor(element),
            backgroundColor = parseBackgroundColor(element),
            letterSpacing = parseLetterSpacing(element),
            lineHeight = parseLineHeight(element),
            indentSize = parseIndentation(element)
        )
    }

    private fun parseStyles(element: Element): Set<TextStyle> {
        val styles = mutableSetOf<TextStyle>()

        // Parse tag-based styles
        when (element.tagName().lowercase()) {
            "b", "strong" -> styles.add(TextStyle.Bold)
            "i", "em" -> styles.add(TextStyle.Italic)
            "u" -> styles.add(TextStyle.Underline)
            "s", "strike", "del" -> styles.add(TextStyle.Strikethrough)
            "sub" -> styles.add(TextStyle.Subscript)
            "sup" -> styles.add(TextStyle.Superscript)
            "code", "pre" -> styles.add(TextStyle.Monospace)
        }

        // Parse class-based styles
        element.classNames().forEach { className ->
            when (className.lowercase()) {
                "bold", "fw-bold" -> styles.add(TextStyle.Bold)
                "italic" -> styles.add(TextStyle.Italic)
                "underline" -> styles.add(TextStyle.Underline)
                "strike" -> styles.add(TextStyle.Strikethrough)
                "small-caps", "smc" -> styles.add(TextStyle.SmallCaps)
                "monospace" -> styles.add(TextStyle.Monospace)
            }
        }

        // Parse inline styles
        parseInlineStyles(element.attr("style"), styles)

        return styles
    }

    private fun parseAlignment(element: Element): TextAlignment {
        // Check class-based alignment
        element.classNames().forEach { className ->
            when (className.lowercase()) {
                "text-left" -> return TextAlignment.LEFT
                "text-center" -> return TextAlignment.CENTER
                "text-right" -> return TextAlignment.RIGHT
                "text-justify" -> return TextAlignment.JUSTIFY
            }
        }

        // Check style-based alignment
        element.attr("style").let { styleAttr ->
            val alignmentRegex = "text-align:\\s*(left|center|right|justify)".toRegex()
            alignmentRegex.find(styleAttr)?.groupValues?.get(1)?.let { alignment ->
                return when (alignment.lowercase()) {
                    "left" -> TextAlignment.LEFT
                    "center" -> TextAlignment.CENTER
                    "right" -> TextAlignment.RIGHT
                    "justify" -> TextAlignment.JUSTIFY
                    else -> TextAlignment.JUSTIFY
                }
            }
        }

        // Default alignment
        return TextAlignment.JUSTIFY
    }

    private fun parseSizeFactor(element: Element): Float {
        // Parse size from class names
        element.classNames().forEach { className ->
            when (className.lowercase()) {
                "text-xs" -> return 0.75f
                "text-sm" -> return 0.875f
                "text-base" -> return 1.0f
                "text-lg" -> return 1.125f
                "text-xl" -> return 1.25f
                "text-2xl" -> return 1.5f
                "text-3xl" -> return 1.875f
            }
        }

        // Parse size from style attribute
        element.attr("style").let { styleAttr ->
            val fontSizeRegex = "font-size:\\s*([\\d.]+)(px|em|rem|%)".toRegex()
            fontSizeRegex.find(styleAttr)?.let { match ->
                val size = match.groupValues[1].toFloatOrNull() ?: return 1.0f
                return when (match.groupValues[2]) {
                    "em", "rem" -> size
                    "%" -> size / 100f
                    "px" -> size / 16f // Assuming base font size is 16px
                    else -> 1.0f
                }
            }
        }

        return 1.0f
    }

    private fun parseColor(element: Element): String? {
        // Check style attribute first
        element.attr("style").let { styleAttr ->
            val colorRegex = "color:\\s*([^;]+)".toRegex()
            colorRegex.find(styleAttr)?.groupValues?.get(1)?.let { return it.trim() }
        }

        // Check color attribute
        element.attr("color").takeIf { it.isNotBlank() }?.let { return it }

        return null
    }

    private fun parseBackgroundColor(element: Element): String? {
        element.attr("style").let { styleAttr ->
            val bgColorRegex = "background-color:\\s*([^;]+)".toRegex()
            bgColorRegex.find(styleAttr)?.groupValues?.get(1)?.let { return it.trim() }
        }
        return null
    }

    private fun parseLetterSpacing(element: Element): Float? {
        element.attr("style").let { styleAttr ->
            val spacingRegex = "letter-spacing:\\s*([\\d.]+)(px|em|rem)".toRegex()
            spacingRegex.find(styleAttr)?.let { match ->
                val spacing = match.groupValues[1].toFloatOrNull() ?: return null
                return when (match.groupValues[2]) {
                    "em", "rem" -> spacing
                    "px" -> spacing / 16f // Convert px to em assuming 16px base
                    else -> null
                }
            }
        }
        return null
    }

    private fun parseLineHeight(element: Element): Float? {
        element.attr("style").let { styleAttr ->
            val lineHeightRegex = "line-height:\\s*([\\d.]+)([a-z%]*)".toRegex()
            lineHeightRegex.find(styleAttr)?.let { match ->
                val height = match.groupValues[1].toFloatOrNull() ?: return null
                return when (match.groupValues[2]) {
                    "", "em", "rem" -> height
                    "%" -> height / 100f
                    "px" -> height / 16f // Convert px to em assuming 16px base
                    else -> null
                }
            }
        }
        return null
    }

    private fun parseInlineStyles(styleAttr: String, styles: MutableSet<TextStyle>) {
        val fontWeightRegex = "font-weight:\\s*(bold|[6-9]00)".toRegex()
        if (fontWeightRegex.containsMatchIn(styleAttr)) {
            styles.add(TextStyle.Bold)
        }

        val fontStyleRegex = "font-style:\\s*italic".toRegex()
        if (fontStyleRegex.containsMatchIn(styleAttr)) {
            styles.add(TextStyle.Italic)
        }

        val textDecorationRegex = "text-decoration:\\s*([^;]+)".toRegex()
        textDecorationRegex.find(styleAttr)?.groupValues?.get(1)?.let { decoration ->
            when {
                decoration.contains("underline") -> styles.add(TextStyle.Underline)
                decoration.contains("line-through") -> styles.add(TextStyle.Strikethrough)
                else -> {}
            }
        }

        val fontVariantRegex = "font-variant:\\s*small-caps".toRegex()
        if (fontVariantRegex.containsMatchIn(styleAttr)) {
            styles.add(TextStyle.SmallCaps)
        }
    }

    private fun parseIndentation(element: Element): Float? {
        // Check for text-indent CSS property
        element.attr("style").let { styleAttr ->
            val indentRegex = "text-indent:\\s*([\\d.]+)(px|em|rem)".toRegex()
            indentRegex.find(styleAttr)?.let { match ->
                val indent = match.groupValues[1].toFloatOrNull() ?: return null
                return when (match.groupValues[2]) {
                    "em", "rem" -> indent
                    "px" -> indent / 16f // Convert px to em assuming 16px base
                    else -> null
                }
            }
        }

        // Check for paragraph or first paragraph class/type
        if (element.tagName() == "p" || element.hasClass("indent")) {
            return 1.5f // Standard paragraph indent
        }

        return null
    }

}
