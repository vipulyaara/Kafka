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

import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.parser.Parser
import io.ktor.http.decodeURLQueryComponent
import okio.Path.Companion.toPath

fun parseXMLText(text: String): Document = Parser.xmlParser().parseInput(text, "")

fun parseXMLFile(bytes: ByteArray): Document =
    Parser.xmlParser().parseInput(bytes.decodeToString(), "")

fun String.decodeURL(): String = decodeURLQueryComponent()

fun String.getCanonicalPath(rootPath: String): String {
    val rootPathObj = rootPath.toPath()
    val fullPath = rootPathObj / this
    return fullPath.normalized().toString().removePrefix("/")
}

fun Document.selectFirstTag(tag: String): Element? = getElementsByTag(tag).firstOrNull()
fun Node.selectFirstChildTag(tag: String) = childElements.find { it.tagName() == tag }
fun Node.selectChildTag(tag: String) = childElements.filter { it.tagName() == tag }
fun Node.getAttributeValue(attribute: String): String? = attr(attribute).takeIf { it.isNotEmpty() }

val List<Node>.elements get() = asSequence().mapNotNull { it as? Element }
val Node.childElements get() = childNodes().elements

fun Node.nextSiblingNodes(): List<Node> {
    val siblings = mutableListOf<Node>()
    var nextSibling = nextSibling()
    while (nextSibling != null) {
        siblings.add(nextSibling)
        nextSibling = nextSibling.nextSibling()
    }
    return siblings
}

fun String.asFileName(): String = this.replace("/", "_")
