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
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.kafka.common.extensions.AnimatedVisibilityFade
import ui.common.theme.theme.Dimens

@Composable
fun InfiniteProgressBar(modifier: Modifier = Modifier, show: Boolean = true) {
    InfiniteProgressBar(modifier, show, Dimens.Spacing08, PaddingValues(Dimens.Gutter), 25f)
}

@Composable
fun InfiniteProgressBarSmall(modifier: Modifier = Modifier, show: Boolean = true) {
    InfiniteProgressBar(modifier, show, Dimens.Spacing06, PaddingValues(Dimens.Spacing04), 15f)
}

@Composable
fun InfiniteProgressBar(
    modifier: Modifier = Modifier,
    show: Boolean = true,
    size: Dp = Dimens.Spacing08,
    padding: PaddingValues = PaddingValues(Dimens.Gutter),
    translation: Float = 25f
) {
    val infiniteTransition = rememberInfiniteTransition()
    AnimatedVisibilityFade(show, modifier) {
        Row(
            modifier = Modifier.padding(padding),
            horizontalArrangement = Arrangement.Center
        ) {
            infiniteTransition.BouncingDot(StartOffset(0), size, translation)
            infiniteTransition.BouncingDot(
                startOffset = StartOffset(150, StartOffsetType.FastForward),
                size = size,
                translation = translation
            )
            infiniteTransition.BouncingDot(
                startOffset = StartOffset(300, StartOffsetType.FastForward),
                size = size,
                translation = translation
            )
        }
    }
}

@Composable
fun InfiniteTransition.BouncingDot(
    startOffset: StartOffset,
    size: Dp,
    translation: Float,
    modifier: Modifier = Modifier
) {
    val bounce by animateFloat(
        -(translation),
        translation,
        infiniteRepeatable(
            tween(600),
            RepeatMode.Reverse,
            initialStartOffset = startOffset
        )
    )
    Box(
        modifier
            .padding(3.dp)
            .size(size)
            .graphicsLayer { translationY = bounce }
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
    )
}
