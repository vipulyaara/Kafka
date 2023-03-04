package org.kafka.ui.components.item

import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun createLayoutTypeVectorPainter(toggle: Boolean): Painter {
    return rememberVectorPainter(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = false
    ) { viewportWidth, viewportHeight ->
        val transition = updateTransition(targetState = toggle, label = "LayoutType")
        val duration = 1000


    }
}
