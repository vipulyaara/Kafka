package org.kafka.ui_common_compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.shadowMaterial(
    elevation: Dp,
    shape: Shape = RectangleShape,
    clip: Boolean = elevation > 0.dp,
    ambientColor: Color = Color(0xFFACB8D2),
    spotColor: Color = Color(0xFFACB8D2)
) = then(shadow(elevation, shape, clip, ambientColor, spotColor).clip(shape))
