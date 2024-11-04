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

object BookTextMapper {

    // <img yrel="{float}"> {uri} </img>
    data class ImgEntry(val path: String, val yrel: Float) {

        /**
         * Deprecated versions: v0
         * Current versions: v1
         */
        companion object {

            fun fromXMLString(text: String): ImgEntry? {
                return fromXMLStringV0(text) ?: fromXMLStringV1(text)
            }

            private fun fromXMLStringV1(text: String): ImgEntry? {
                return Ksoup.parse(text).selectFirst("img")?.let {
                    ImgEntry(
                        path = it.attr("src"),
                        yrel = it.attr("yrel").toFloatOrNull() ?: return null
                    )
                }
            }

            private val XMLForm_v0 = """^\W*<img .*>.+</img>\W*$""".toRegex()

            private fun fromXMLStringV0(text: String): ImgEntry? {
                // Fast discard filter
                if (!text.matches(XMLForm_v0))
                    return null
                return parseXMLText(text).selectFirstTag("img")?.let { node ->
                    ImgEntry(
                        path = node.text(),
                        yrel = node.getAttributeValue("yrel")?.toFloatOrNull() ?: return null
                    )
                }
            }
        }

        fun toXMLString(): String {
            return toXMLStringV1()
        }

        private fun toXMLStringV1(): String {
            val roundedYrel = (yrel * 100).toInt() / 100.0  // Rounds to 2 decimal places
            return """<img src="$path" yrel="$roundedYrel">"""
        }

        /*
        private fun toXMLStringV0(): String {
            return """<img yrel="${"%.2f".format(yrel)}">$path</img>"""
        }
        */
    }
}