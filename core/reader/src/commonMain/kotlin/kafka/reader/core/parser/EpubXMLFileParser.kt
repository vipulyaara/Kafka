/**
 * Copyright (c) [2022 - Present] Stɑrry Shivɑm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package kafka.reader.core.parser

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import com.kafka.base.debug
import kafka.reader.core.models.ContentElement
import okio.Path
import okio.Path.Companion.toPath

/**
 * Parses an XML file from an EPUB archive and extracts the title and body content.
 *
 * @property fileAbsolutePath The absolute path of the XML file.
 * @property data The raw data of the XML file.
 * @property zipFile The map of file paths to their respective [EpubParser.EpubFile] instances.
 * @property fragmentId The ID of the fragment to extract from the XML file.
 * @property nextFragmentId The ID of the next fragment to extract from the XML file.
 */
class EpubXMLFileParser(
    val fileAbsolutePath: String,
    val data: ByteArray,
    private val zipFile: Map<String, EpubParser.EpubFile>,
    private val fragmentId: String? = null,
    private val nextFragmentId: String? = null
) {

    // The parent folder of the XML file.
    private val fileParentFolder: Path = fileAbsolutePath.toPath().parent ?: "".toPath()


    /**
     * Parses the input data as an XML document and returns content elements.
     *
     * @return List<ContentElement> The parsed content elements.
     */
    fun parseAsDocument(): List<ContentElement> {
        val document = Ksoup.parse(data.decodeToString(), "")
        
        return when {
            fragmentId != null -> {
                val divElement = document.selectFirst("div#$fragmentId")
                if (divElement != null) {
                    debug { "Fragment ID: $fragmentId represents a <div> tag" }
                    ContentParser.parseNode(divElement, ::parseImageElement)
                } else {
                    debug { "Using fragment and next fragment logic" }
                    parseFragmentContent(document)
                }
            }
            else -> {
                debug { "Parsing entire document" }
                ContentParser.parseNode(document.body(), ::parseImageElement)
            }
        }
    }

    private fun parseFragmentContent(document: Document): List<ContentElement> {
        val fragmentElement = document.selectFirst("#$fragmentId") ?: return emptyList()
        
        // If next fragment exists, get all content between fragment and next
        return if (nextFragmentId != null) {
            val nextElement = document.selectFirst("#$nextFragmentId")
            val elements = mutableListOf<Node>()
            
            var current: Node? = fragmentElement
            while (current != null && current != nextElement) {
                elements.add(current)
                current = getNextSibling(current)
            }
            
            elements.flatMap { ContentParser.parseNode(it, ::parseImageElement) }
        } else {
            // Just parse from fragment to end
            ContentParser.parseNode(fragmentElement, ::parseImageElement)
        }
    }

    /**
     * Parses the input data as an image and returns the image path and aspect ratio.
     *
     * @param absolutePathImage The absolute path of the image file.
     * @return [String] The image path and aspect ratio.
     */
    fun parseAsImage(absolutePathImage: String): String {
        val imageData = zipFile[absolutePathImage]?.data
        val aspectRatio = imageData?.let { ImageDecoder.getAspectRatio(it) } ?: 1.45f

        val text = BookTextMapper.ImgEntry(
            path = absolutePathImage,
            yrel = aspectRatio
        ).toXMLString()

        return "\n\n$text\n\n"
    }

    // Traverses the XML document to find the next sibling node.
    private fun getNextSibling(currentNode: Node?): Node? {
        var nextSibling: Node? = currentNode?.nextSibling()

        if (nextSibling == null) {
            var parentNode = currentNode?.parent()
            while (parentNode != null) {
                nextSibling = parentNode.nextSibling()
                if (nextSibling != null) {
                    // If the parent's next sibling is not null, traverse its descendants
                    // to find the next node
                    return traverseDescendants(nextSibling)
                }
                parentNode = parentNode.parent()
            }
        }

        return nextSibling
    }

    // Traverses the descendants of a node to find the next node.
    private fun traverseDescendants(node: Node): Node? {
        val children = node.childNodes()
        if (children.isNotEmpty()) {
            return children.first()
        }

        val siblings = node.nextSiblingNodes()
        if (siblings.isNotEmpty()) {
            return traverseDescendants(siblings.first())
        }

        return null
    }

    // Rewrites the image node to xml for the next stage.
    private fun declareImgEntry(node: Node): String {
        val attrs = node.attributes().associate { it.key to it.value }
        val relPathEncoded = attrs["src"] ?: attrs["xlink:href"] ?: ""

        val absolutePathImage = (fileParentFolder / relPathEncoded.decodeURL())
            .normalized()
            .toString()
            .removePrefix("/")

        return parseAsImage(absolutePathImage)
    }

    private fun parseImageElement(node: Node): ContentElement.Image {
        val attrs = node.attributes().associate { it.key to it.value }
        val relPathEncoded = attrs["src"] ?: attrs["xlink:href"] ?: ""

        val absolutePathImage = (fileParentFolder / relPathEncoded.decodeURL())
            .normalized()
            .toString()
            .removePrefix("/")

        val imageData = zipFile[absolutePathImage]?.data
        val aspectRatio = imageData?.let { ImageDecoder.getAspectRatio(it) } ?: 1.45f

        return ContentElement.Image(
            path = absolutePathImage,
            caption = attrs["alt"],
            data = imageData,
            aspectRatio = aspectRatio
        )
    }
}