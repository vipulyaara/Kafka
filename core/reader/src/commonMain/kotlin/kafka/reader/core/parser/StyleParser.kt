package kafka.reader.core.parser

import com.fleeksoft.ksoup.nodes.Element
import kafka.reader.core.models.TextAlignment
import kafka.reader.core.models.TextStyle

internal data class StyleProperties(
    val styles: Set<TextStyle> = setOf(),
    val sizeFactor: Float = 1.0f,
    val color: String? = null,
    val backgroundColor: String? = null,
    val letterSpacing: Float? = null,
    val lineHeight: Float? = null,
    val alignment: TextAlignment = TextAlignment.JUSTIFY
)

internal fun parseStyles(element: Element): StyleProperties {
    val cssClasses = parseCssClasses(element)
    val inlineStyles = parseInlineStyles(element)
    val tagStyles = parseTagStyles(element)

    return StyleProperties(
        styles = (cssClasses.styles + inlineStyles.styles + tagStyles.styles).takeIf { it.isNotEmpty() }
            ?: setOf(TextStyle.Normal),
        sizeFactor = maxOf(
            cssClasses.sizeFactor,
            inlineStyles.sizeFactor,
            tagStyles.sizeFactor
        ),
        color = inlineStyles.color ?: cssClasses.color,
        backgroundColor = inlineStyles.backgroundColor ?: cssClasses.backgroundColor,
        letterSpacing = inlineStyles.letterSpacing ?: cssClasses.letterSpacing,
        lineHeight = inlineStyles.lineHeight ?: cssClasses.lineHeight,
        alignment = inlineStyles.alignment.takeIf { it != TextAlignment.LEFT }
            ?: cssClasses.alignment
    )
}

private fun parseCssClasses(element: Element): StyleProperties {
    val styles = mutableSetOf<TextStyle>()
    var sizeFactor = 1.0f
    var color: String? = null
    var backgroundColor: String? = null
    var alignment = TextAlignment.LEFT

    element.classNames().forEach { className ->
        when (className.lowercase()) {
            // Text styles
            "bold", "strong", "fw-bold" -> styles.add(TextStyle.Bold)
            "italic", "em", "fst-italic" -> styles.add(TextStyle.Italic)
            "underline" -> styles.add(TextStyle.Underline)
            "strike", "strikethrough", "line-through" -> styles.add(TextStyle.Strikethrough)
            "small-caps", "caps" -> styles.add(TextStyle.SmallCaps)
            "monospace", "code" -> styles.add(TextStyle.Monospace)

            // Size classes
            "text-xs" -> sizeFactor = 0.75f
            "text-sm" -> sizeFactor = 0.875f
            "text-lg" -> sizeFactor = 1.125f
            "text-xl" -> sizeFactor = 1.25f
            "text-2xl" -> sizeFactor = 1.5f
            "text-3xl" -> sizeFactor = 1.875f
            "text-4xl" -> sizeFactor = 2.25f

            // Alignment
            "text-center" -> alignment = TextAlignment.CENTER
            "text-right" -> alignment = TextAlignment.RIGHT
            "text-justify" -> alignment = TextAlignment.JUSTIFY

            // Common EPUB classes
            "title", "heading" -> {
                styles.add(TextStyle.Heading1)
                sizeFactor = 2.0f
            }

            "subtitle" -> {
                styles.add(TextStyle.Heading2)
                sizeFactor = 1.5f
            }

            "chapter-title" -> {
                styles.add(TextStyle.Heading2)
                sizeFactor = 1.75f
            }

            "dropcap", "initial" -> {
                styles.add(TextStyle.Heading1)
                sizeFactor = 3.0f
            }
        }
    }

    return StyleProperties(
        styles = styles,
        sizeFactor = sizeFactor,
        color = color,
        backgroundColor = backgroundColor,
        alignment = alignment
    )
}

private fun parseInlineStyles(element: Element): StyleProperties {
    val styles = mutableSetOf<TextStyle>()
    var sizeFactor = 1.0f
    var color: String? = null
    var backgroundColor: String? = null
    var letterSpacing: Float? = null
    var lineHeight: Float? = null
    var alignment = TextAlignment.LEFT

    val styleAttr = element.attr("style")
    if (styleAttr.isNotEmpty()) {
        styleAttr.split(";").forEach { declaration ->
            val parts = declaration.split(":")
            if (parts.size == 2) {
                val property = parts[0].trim().lowercase()
                val value = parts[1].trim().lowercase()

                when (property) {
                    "font-weight" -> {
                        when {
                            value == "bold" || value.toIntOrNull()?.let { it >= 600 } == true ->
                                styles.add(TextStyle.Bold)
                        }
                    }

                    "font-style" -> {
                        if (value == "italic") styles.add(TextStyle.Italic)
                    }

                    "text-decoration" -> {
                        when (value) {
                            "underline" -> styles.add(TextStyle.Underline)
                            "line-through" -> styles.add(TextStyle.Strikethrough)
                        }
                    }

                    "font-variant" -> {
                        if (value == "small-caps") styles.add(TextStyle.SmallCaps)
                    }

                    "font-size" -> {
                        sizeFactor = when {
                            value.endsWith("em") -> value.removeSuffix("em").toFloatOrNull() ?: 1f
                            value.endsWith("rem") -> value.removeSuffix("rem").toFloatOrNull() ?: 1f
                            value.endsWith("%") -> value.removeSuffix("%").toFloatOrNull()?.div(100)
                                ?: 1f

                            value.endsWith("px") -> {
                                val px = value.removeSuffix("px").toFloatOrNull() ?: 16f
                                px / 16f  // Assuming 16px is base size
                            }

                            value.endsWith("pt") -> {
                                val pt = value.removeSuffix("pt").toFloatOrNull() ?: 12f
                                pt / 12f  // Assuming 12pt is base size
                            }

                            else -> 1f
                        }
                    }

                    "color" -> color = value
                    "background-color" -> backgroundColor = value
                    "letter-spacing" -> {
                        letterSpacing = when {
                            value.endsWith("em") -> value.removeSuffix("em").toFloatOrNull()
                            value.endsWith("px") -> value.removeSuffix("px").toFloatOrNull()
                                ?.div(16f)

                            else -> null
                        }
                    }

                    "line-height" -> {
                        lineHeight = value.toFloatOrNull()
                    }

                    "text-align" -> {
                        alignment = when (value) {
                            "center" -> TextAlignment.CENTER
                            "right" -> TextAlignment.RIGHT
                            "justify" -> TextAlignment.JUSTIFY
                            else -> TextAlignment.LEFT
                        }
                    }
                }
            }
        }
    }

    return StyleProperties(
        styles = styles,
        sizeFactor = sizeFactor,
        color = color,
        backgroundColor = backgroundColor,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight,
        alignment = alignment
    )
}

private fun parseTagStyles(element: Element): StyleProperties {
    val styles = mutableSetOf<TextStyle>()
    var sizeFactor = 1.0f

    when (element.tagName().lowercase()) {
        "strong", "b" -> styles.add(TextStyle.Bold)
        "em", "i" -> styles.add(TextStyle.Italic)
        "u" -> styles.add(TextStyle.Underline)
        "s", "strike", "del" -> styles.add(TextStyle.Strikethrough)
        "sub" -> {
            styles.add(TextStyle.Subscript)
            sizeFactor = 0.8f
        }

        "sup" -> {
            styles.add(TextStyle.Superscript)
            sizeFactor = 0.8f
        }

        "code", "pre" -> styles.add(TextStyle.Monospace)
        "h1" -> {
            styles.add(TextStyle.Heading1)
            sizeFactor = 2.0f
        }

        "h2" -> {
            styles.add(TextStyle.Heading2)
            sizeFactor = 1.75f
        }

        "h3" -> {
            styles.add(TextStyle.Heading3)
            sizeFactor = 1.5f
        }

        "h4" -> {
            styles.add(TextStyle.Heading4)
            sizeFactor = 1.25f
        }

        "h5" -> {
            styles.add(TextStyle.Heading5)
            sizeFactor = 1.1f
        }

        "h6" -> {
            styles.add(TextStyle.Heading6)
            sizeFactor = 1.0f
        }
    }

    return StyleProperties(styles = styles, sizeFactor = sizeFactor)
}
