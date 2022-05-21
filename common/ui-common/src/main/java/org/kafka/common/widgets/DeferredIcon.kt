package org.kafka.common.widgets

import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import ui.common.theme.theme.ProvideRipple

@Composable
fun IconResource(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    contentDescription: String? = null
) {
    ProvideRipple(isBounded = false) {
        Icon(
            imageVector = imageVector,
            modifier = modifier,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun IconResource(
    imageVector: Painter,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    contentDescription: String? = null
) {
    Icon(
        painter = imageVector,
        modifier = modifier,
        tint = tint,
        contentDescription = contentDescription
    )
}
