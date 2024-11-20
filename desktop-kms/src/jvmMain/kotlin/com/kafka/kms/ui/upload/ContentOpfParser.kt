package com.kafka.kms.ui.upload

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

data class ContentOpfMetadata(
    val title: String = "",
    val creators: List<String> = emptyList(),
    val description: String = "",
    val longDescription: String = "",
    val subjects: List<String> = emptyList(),
    val publishers: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val rights: String = "",
    val contributors: List<String> = emptyList(),
    val translators: List<String> = emptyList(),
    val sources: List<String> = emptyList()
)

object ContentOpfParser {
    fun parse(filePath: String): ContentOpfMetadata {
        val file = File(filePath)
        val document = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(file)

        return document.documentElement.let { root ->
            val metadata = root.getElementsByTagName("metadata").item(0) as Element

            val translators = metadata.getElements("dc:contributor").filter { contributor ->
                val id = contributor.getAttribute("id")
                metadata.getElementsByTagName("meta")
                    .toElementList()
                    .any { meta ->
                        meta.getAttribute("refines") == "#$id" &&
                                meta.getAttribute("property") == "role" &&
                                meta.getAttribute("scheme") == "marc:relators" &&
                                meta.textContent == "trl"
                    }
            }.map { it.textContent }

            val contributors = metadata.getElements("dc:contributor").filter { contributor ->
                val id = contributor.getAttribute("id")
                metadata.getElementsByTagName("meta")
                    .toElementList()
                    .none { meta ->
                        meta.getAttribute("refines") == "#$id" &&
                                meta.getAttribute("property") == "role" &&
                                meta.getAttribute("scheme") == "marc:relators" &&
                                meta.textContent == "trl"
                    }
            }.map { it.textContent }

            val identifierUrl = metadata.getElementsByTagName("dc:identifier")
                .toElementList()
                .firstOrNull()
                ?.textContent
                ?.removePrefix("url:")
                ?.trim()

            val sourceUrls = metadata.getElements("dc:source")
                .map { it.textContent.trim() }

            val allSources = (listOfNotNull(identifierUrl) + sourceUrls)
                .filter { it.isNotEmpty() }
                .distinct()

            ContentOpfMetadata(
                title = metadata.getTextContent("dc:title"),
                creators = metadata.getElements("dc:creator").map { it.textContent },
                description = metadata.getTextContent("dc:description"),
                longDescription = metadata.getElementsByTagName("meta")
                    .findByProperty("se:long-description")
                    ?.textContent ?: "",
                subjects = metadata.getElements("dc:subject").map { it.textContent },
                publishers = metadata.getElements("dc:publisher").map { it.textContent },
                languages = metadata.getElements("dc:language").map { it.textContent },
                rights = metadata.getTextContent("dc:rights"),
                contributors = contributors,
                translators = translators,
                sources = allSources
            )
        }
    }

    private fun Element.getTextContent(tagName: String): String =
        getElementsByTagName(tagName)
            .item(0)
            ?.textContent ?: ""

    private fun Element.getElements(tagName: String): List<Element> =
        getElementsByTagName(tagName)
            .toElementList()

    private fun NodeList.findByProperty(propertyValue: String): Element? =
        toElementList()
            .find { it.getAttribute("property") == propertyValue }

    private fun NodeList.toElementList(): List<Element> =
        (0 until length).map { item(it) as Element }
} 