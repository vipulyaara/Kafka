package org.kafka.common.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dropdownStroke(
    borderSize: Dp = 2.dp,
    containerColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified
) = composed {
    val container =
        if (containerColor == Color.Unspecified)
            MaterialTheme.colorScheme.surface else containerColor
    val stroke =
        if (borderColor == Color.Unspecified)
            MaterialTheme.colorScheme.primary else containerColor

    background(container)
    border(borderSize, stroke)
}
