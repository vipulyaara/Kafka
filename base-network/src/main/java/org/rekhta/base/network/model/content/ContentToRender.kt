package org.rekhta.base.network.model.content

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

@Serializable
@Immutable
data class ContentToRender(
    @SerialName("P")
    var paras: List<Para>? = null
) {
    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        fun mapFrom(data: String): ContentToRender? {
            val cleanedData = cleanJsoup(data)
            return json.decodeFromString(cleanedData)
        }
    }
}

@Serializable
data class Para(
    @SerialName("L")
    var lines: List<Line>? = null,
    @SerialName("T")
    var t: List<Line>? = null
)

@Serializable
data class Line(
    @SerialName("W")
    var words: List<Word>? = null
)

@Serializable
data class Word(
    @SerialName("M")
    var m: String? = null,
    @SerialName("W")
    var w: String? = null,
    @SerialName("S")
    var s: String? = null
) {
    val word
        get() = w.orEmpty()
    val mId
        get() = m.orEmpty()
}

fun ContentToRender.textWithExtras(poetName: String, contentUrl: String) =
    "$text\n\n$poetName\n$contentUrl"

val ContentToRender.text: String
    get() = buildString {
        paras?.forEachIndexed { paraIndex, para ->
            append(para.text)
            if (paraIndex != paras?.lastIndex) {
                append("\n\n")
            }
        }
    }

val Para.text: String
    get() = buildString {
        lines?.forEachIndexed { lineIndex, line ->
            append(line.words?.joinToString(" ") { it.w.orEmpty() }.orEmpty())
            if (lineIndex != lines?.lastIndex) {
                append("\n")
            }
        }
    }


fun cleanJsoup(data: String?) = Jsoup.clean(
    Jsoup.parse(data).text()
        .replace("&nbsp;", " "), Whitelist.none()
)
