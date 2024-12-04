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
    val sources: List<String> = emptyList(),
    val collections: List<String> = emptyList(),
    val isCopyrighted: Boolean = false
)

object ContentOpfParser {
    private const val SE_PRODUCTION_NOTES_IDENTIFIER = "production-notes"
    private const val SE_URL_PREFIX = "https://standardebooks.org/ebooks/"
    
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

            val sourceUrls = buildList {
                addAll(metadata.getElements("dc:source")
                    .map { it.textContent.trim() }
                    .filter { it.isNotEmpty() })

                metadata.getElementsByTagName("dc:identifier")
                    .toElementList()
                    .firstOrNull()
                    ?.textContent
                    ?.removePrefix("url:")
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { add(it) }

                if (metadata.getElementsByTagName("meta")
                    .toElementList()
                    .any { it.getAttribute("property") == SE_PRODUCTION_NOTES_IDENTIFIER }
                ) {
                    val seUrl = metadata.getElementsByTagName("dc:identifier")
                        .toElementList()
                        .firstOrNull { it.textContent.startsWith(SE_URL_PREFIX) }
                        ?.textContent
                        ?: metadata.getElementsByTagName("dc:identifier")
                            .toElementList()
                            .firstOrNull { it.getAttribute("id") == "uid" }
                            ?.textContent
                            ?.let { "$SE_URL_PREFIX$it" }
                    
                    seUrl?.let { add(it) }
                }
            }.distinct()

            val rights = metadata.getTextContent("dc:rights").trim()
            
            val isCopyrighted = when {
                rights.isEmpty() -> true
                rights.contains("public domain", ignoreCase = true) -> false
                rights.contains("CC0", ignoreCase = true) -> false
                rights.contains("copyrighted", ignoreCase = true) -> true
                rights.contains("copyright", ignoreCase = true) -> true
                else -> true
            }

            val collections = metadata.getElementsByTagName("meta")
                .toElementList()
                .filter { meta ->
                    meta.getAttribute("property") == "belongs-to-collection"
                }
                .map { it.textContent }
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
                rights = rights,
                contributors = contributors,
                translators = translators,
                sources = sourceUrls,
                collections = collections,
                isCopyrighted = isCopyrighted
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