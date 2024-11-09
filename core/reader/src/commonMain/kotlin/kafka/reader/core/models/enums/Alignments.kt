package kafka.reader.core.models.enums

enum class ColumnAlignment {
    LEFT,
    CENTER,
    RIGHT
}

enum class TextAlignment {
    LEFT,
    CENTER,
    RIGHT,
    JUSTIFY
}

fun ColumnAlignment.toTextAlignment() = when (this) {
    ColumnAlignment.LEFT -> TextAlignment.LEFT
    ColumnAlignment.CENTER -> TextAlignment.CENTER
    ColumnAlignment.RIGHT -> TextAlignment.RIGHT
}
