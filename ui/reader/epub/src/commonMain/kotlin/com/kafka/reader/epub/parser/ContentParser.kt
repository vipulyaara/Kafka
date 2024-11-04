package com.kafka.reader.epub.parser

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import com.kafka.reader.epub.models.ContentElement
import com.kafka.reader.epub.models.TextStyle

object ContentParser {
    fun parseNode(node: Node, imageParser: ((Node) -> ContentElement.Image)? = null): List<ContentElement> {
        return buildList {
            node.childNodes().forEach { child ->
                when (child) {
                    is TextNode -> {
                        val text = child.text().trim()
                        if (text.isNotEmpty()) {
                            add(ContentElement.Text(text))
                        }
                    }

                    is Element -> {
                        when (child.tagName().lowercase()) {
                            "h1" -> add(ContentElement.Text(
                                child.text().trim(),
                                style = TextStyle.Heading1
                            ))
                            "h2" -> add(ContentElement.Text(
                                child.text().trim(),
                                style = TextStyle.Heading2
                            ))
                            "h3" -> add(ContentElement.Text(
                                child.text().trim(),
                                style = TextStyle.Heading3
                            ))
                            "h4" -> add(ContentElement.Text(
                                child.text().trim(),
                                style = TextStyle.Heading4
                            ))
                            "h5" -> add(ContentElement.Text(
                                child.text().trim(),
                                style = TextStyle.Heading5
                            ))
                            "h6" -> add(ContentElement.Text(
                                child.text().trim(),
                                style = TextStyle.Heading6
                            ))

                            "p" -> {
                                val content = parseInlineContent(child).trim()
                                if (content.isNotEmpty()) {
                                    add(
                                        ContentElement.Text(
                                            content = content,
                                            style = parseTextStyle(child)
                                        )
                                    )
                                }
                            }

                            "blockquote" -> {
                                val attribution =
                                    child.selectFirst("cite")?.let { parseInlineContent(it) }
                                add(
                                    ContentElement.Quote(
                                        content = parseInlineContent(child),
                                        attribution = attribution
                                    )
                                )
                            }

                            "img", "image" -> {
                                imageParser?.let { parser ->
                                    add(parser(child))
                                }
                            }

                            "ul", "ol" -> {
                                add(
                                    ContentElement.List(
                                        items = child.select("li").map { parseInlineContent(it) },
                                        ordered = child.tagName() == "ol",
                                        startIndex = child.attr("start").toIntOrNull() ?: 1
                                    )
                                )
                            }

                            "pre", "code" -> {
                                add(ContentElement.CodeBlock(
                                    content = child.text(),
                                    language = child.attr("class")
                                        .split(" ")
                                        .firstOrNull { it.startsWith("language-") }
                                        ?.removePrefix("language-")
                                ))
                            }

                            "hr" -> add(ContentElement.Divider)

                            "table" -> {
                                val headers = child.select("th").map { parseInlineContent(it) }
                                val rows = child.select("tr").map { row ->
                                    row.select("td").map { parseInlineContent(it) }
                                }.filter { it.isNotEmpty() }
                                add(ContentElement.Table(headers, rows))
                            }

                            // Add support for more inline elements
                            "a" -> {
                                add(
                                    ContentElement.Link(
                                        content = parseInlineContent(child),
                                        href = child.attr("href")
                                    )
                                )
                            }

                            "span", "div" -> {
                                val nestedElements = parseNode(child)
                                if (nestedElements.isNotEmpty()) {
                                    addAll(nestedElements)
                                }
                            }

                            "div", "section" -> {
                                // Handle div/section containers
                                addAll(parseNode(child, imageParser))
                            }

                            "caption" -> {
                                // Handle captions with italic style
                                add(ContentElement.Text(
                                    child.text().trim(),
                                    style = TextStyle.Italic
                                ))
                            }

                            "ins" -> {
                                // Handle inserted text with title attribute
                                val title = child.attr("title")
                                val text = child.text().trim()
                                if (text.isNotEmpty()) {
                                    add(ContentElement.Text(text))
                                }
                            }

                            "span" -> {
                                // Handle spans with special classes
                                when {
                                    child.hasClass("smc") -> {
                                        add(ContentElement.Text(
                                            child.text().trim(),
                                            style = TextStyle.SmallCaps
                                        ))
                                    }
                                    else -> addAll(parseNode(child, imageParser))
                                }
                            }

                            "strong" -> {
                                // Map strong tag to Heading3 style for emphasis
                                add(ContentElement.Text(
                                    child.text().trim(),
                                    style = TextStyle.Heading3
                                ))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun parseInlineContent(element: Element): String {
        return buildString {
            element.childNodes().forEach { node ->
                when (node) {
                    is TextNode -> {
                        val text = node.text()
                        if (text.isNotBlank()) {
                            if (isNotEmpty() && !last().isWhitespace() && !text.first()
                                    .isWhitespace()
                            ) {
                                append(' ')
                            }
                            append(text.trim())
                        }
                    }

                    is Element -> {
                        val content = parseInlineContent(node)
                        if (content.isNotBlank()) {
                            if (isNotEmpty() && !last().isWhitespace() && !content.first()
                                    .isWhitespace()
                            ) {
                                append(' ')
                            }
                            append(content.trim())
                        }
                    }
                }
            }
        }.trim()
    }

    private fun parseTextStyle(element: Element): TextStyle {
        return when {
            element.hasClass("bold") || element.tagName() == "strong" -> TextStyle.Bold
            element.hasClass("italic") || element.tagName() == "em" -> TextStyle.Italic
            element.hasClass("underline") || element.tagName() == "u" -> TextStyle.Underline
            element.hasClass("strikethrough") || element.tagName() == "s" -> TextStyle.Strikethrough
            element.hasClass("subscript") || element.tagName() == "sub" -> TextStyle.Subscript
            element.hasClass("superscript") || element.tagName() == "sup" -> TextStyle.Superscript
            else -> TextStyle.Normal
        }
    }

    private fun parseTableContent(table: Element): List<ContentElement> {
        return buildList {
            // Add caption if present
            table.select("caption").firstOrNull()?.let { caption ->
                add(ContentElement.Text(
                    caption.text().trim(),
                    style = TextStyle.Bold
                ))
            }
            
            // Parse rows
            table.select("tr").forEach { row ->
                // Handle cells
                row.children().forEach { cell ->
                    addAll(parseNode(cell))
                    add(ContentElement.Text("\t"))
                }
                add(ContentElement.Text("\n"))
            }
        }
    }
}
