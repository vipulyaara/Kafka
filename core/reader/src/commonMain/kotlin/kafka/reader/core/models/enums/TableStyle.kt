package kafka.reader.core.models.enums

import kotlinx.serialization.Serializable

@Serializable
enum class TableStyle {
    Borderless,
    Bordered,
    Striped,
    BorderedAndStriped,
    Minimal,
    Compact;
} 