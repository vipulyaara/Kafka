package org.kafka.ui.components.progress

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun InfiniteProgressBar(modifier: Modifier = Modifier, show: Boolean = true) {
    val infiniteTransition = rememberInfiniteTransition()
    if (show) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            infiniteTransition.BouncingDot(StartOffset(0))
            infiniteTransition.BouncingDot(StartOffset(150, StartOffsetType.FastForward))
            infiniteTransition.BouncingDot(StartOffset(300, StartOffsetType.FastForward))
        }
    }
}

@Composable
fun InfiniteTransition.BouncingDot(startOffset: StartOffset, modifier: Modifier = Modifier) {
    val bounce by animateFloat(
        0f,
        50f,
        infiniteRepeatable(
            tween(600),
            RepeatMode.Reverse,
            initialStartOffset = startOffset
        )
    )
    Box(
        modifier
            .padding(3.dp)
            .size(8.dp)
            .graphicsLayer {
                translationY = bounce
            }
            .background(MaterialTheme.colorScheme.onSurface, shape = CircleShape)
    )
}
