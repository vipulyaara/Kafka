package org.kafka.ui.components.progress

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ui.common.theme.theme.Dimens

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
fun InfiniteTransition.BouncingDot(
    startOffset: StartOffset,
    modifier: Modifier = Modifier
) {
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
            .size(Dimens.Spacing08)
            .graphicsLayer { translationY = bounce }
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
    )
}
