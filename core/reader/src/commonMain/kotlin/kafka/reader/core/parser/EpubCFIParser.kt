package kafka.reader.core.parser

object EpubCFIParser {
    private val CFI_REGEX = """epubcfi\(/6/4\[([^\]]+)\]!/4(/\d+/\d+/\d+/\d+/\d+)*:(\d+),/6/4\[[^\]]+\]!/4(?:/\d+/\d+/\d+/\d+/\d+)*:(\d+)\)""".toRegex()

    data class CFIPath(
        val elementPath: List<Int>,
        val offset: Int
    )

    data class CFILocation(
        val chapterId: String,
        val elementPath: List<Int> = emptyList(),
        val startOffset: Int,
        val endOffset: Int
    )

    fun generate(
        chapterId: String,
        elementPath: List<Int>,
        startOffset: Int,
        endOffset: Int
    ): String {
        val pathString = elementPath.joinToString("") { "/$it" }
        return buildString {
            append("epubcfi(/6/4[")
            append(chapterId)
            append("]!/4")
            append(pathString)
            append(":")
            append(startOffset)
            append(",/6/4[")
            append(chapterId)
            append("]!/4")
            append(pathString)
            append(":")
            append(endOffset)
            append(")")
        }
    }

    fun parse(cfi: String): CFILocation? {
        return CFI_REGEX.matchEntire(cfi)?.let { match ->
            val (chapterId, path, startOffset, endOffset) = match.destructured
            CFILocation(
                chapterId = chapterId,
                elementPath = path.split("/")
                    .filter { it.isNotEmpty() }
                    .map { it.toInt() },
                startOffset = startOffset.toInt(),
                endOffset = endOffset.toInt()
            )
        }
    }
} 