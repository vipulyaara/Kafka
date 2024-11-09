package kafka.reader.core.models.enums

import kotlinx.serialization.Serializable

@Serializable
enum class TableStyle {
    Default,
    Bordered,
    Striped,
    BorderedAndStriped,
    Minimal,
    Compact;
} 