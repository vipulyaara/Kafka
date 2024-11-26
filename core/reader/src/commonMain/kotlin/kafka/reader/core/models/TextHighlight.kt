package kafka.reader.core.models

import kafka.reader.core.parser.EpubCFIParser
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class TextHighlight(
    val bookId: String,
    val chapterId: String,
    val text: String,
    val cfiRange: String,
    val color: String,
    val note: String? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
) {
    val id: String
        get() = "$bookId-$chapterId-${cfiRange.hashCode()}"

    val startOffset: Int
        get() = parsedCfi.startOffset

    val endOffset: Int
        get() = parsedCfi.endOffset

    private val parsedCfi: EpubCFIParser.CFILocation by lazy {
        EpubCFIParser.parse(cfiRange) ?: EpubCFIParser.CFILocation(chapterId, emptyList(), 0, 0)
    }

    enum class Color(val code: String) {
        YELLOW("#FFEB3B"),
        GREEN("#A5D6A7"),
        BLUE("#90CAF9"),
        PINK("#F48FB1"),
        PURPLE("#CE93D8");

        companion object {
            fun fromCode(code: String) = entries.find { it.code == code } ?: YELLOW
        }
    }
}
