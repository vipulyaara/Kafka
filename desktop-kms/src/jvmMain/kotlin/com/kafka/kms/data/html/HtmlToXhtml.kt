package com.kafka.kms.data.html

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.parser.Parser
import com.kafka.base.debug

fun convertToXhtml(html: String): String {
    val doc = Ksoup.parse(html, Parser.xmlParser())

    debug { "htm parsing $doc" }

    // Find the chapter div first
    val chapterDiv = doc.getElementsByClass("chapter").first()
    
    // Remove all complete tags that appear before the chapter div
    if (chapterDiv != null) {
        val body = doc.body()
        val elements = body.children()
        val iterator = elements.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            if (element == chapterDiv) {
                break
            }
            // Remove the element if it doesn't contain the chapter div
            if (!element.getElementsByClass("chapter").any()) {
                element.remove()
            }
        }
    }

    // Remove head tag
    doc.head()?.remove()

    // Remove complete tags after the end chapter comment
    val endChapterComment = doc.getElementsByTag("body")
        .first()
        ?.getElementsByTag("div")
        ?.first { it.hasClass("chapter") }
        ?.nextSibling()
    
    if (endChapterComment != null && endChapterComment.toString().contains("end chapter")) {
        var current = endChapterComment.nextSibling()
        while (current != null) {
            val next = current.nextSibling()
            if (current is Element) {
                current.remove()
            }
            current = next
        }
    }

    // Format as XHTML
    doc.outputSettings()
        .syntax(Document.OutputSettings.Syntax.xml)
        .prettyPrint(true)
        .indentAmount(2)

    return doc.html()
}