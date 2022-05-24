package org.kafka.common.widgets

import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
        Image(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            colorFilter = ColorFilter.tint(tint)
        )
    }
}
