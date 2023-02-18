package org.kafka.common.widgets

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.common.theme.theme.shadowMaterial

fun Modifier.shadowMaterial(
    elevation: Dp,
    shape: Shape = RectangleShape,
    clip: Boolean = elevation > 0.dp,
    ambientColor: Color? = null,
    spotColor: Color? = null
) = composed {
    shadow(
        elevation = elevation,
        shape = shape,
        clip = clip,
        ambientColor = ambientColor ?: MaterialTheme.colorScheme.shadowMaterial,
        spotColor = spotColor ?: MaterialTheme.colorScheme.shadowMaterial
    ).background(MaterialTheme.colorScheme.surface)
}
