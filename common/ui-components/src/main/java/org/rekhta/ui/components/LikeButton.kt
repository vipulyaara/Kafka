package org.rekhta.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import org.kafka.common.Icons
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.widgets.IconResource
import ui.common.theme.theme.ProvideRipple
import ui.common.theme.theme.iconPrimary

@Composable
fun LikeButton(
    isLiked: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = colorScheme.iconPrimary,
    onClicked: (() -> Unit)? = null
) {
    var playSparkles by rememberMutableState { false }

    val favoriteColor by animateColorAsState(if (isLiked) colorScheme.primary else tint)
    val favoriteIcon = if (isLiked) Icons.HeartFilled else Icons.Heart

    val sizeCoefficient = 1f
//    val sizeCoefficient by animateFloatAsState(
//        targetValue = if (playSparkles) 1.01f else 1f,
//        animationSpec = keyframes {
//            durationMillis = 322
//            1f at 0 with LinearOutSlowInEasing
//            0.7f at 75 with LinearOutSlowInEasing
//            1.4f at 150 with LinearOutSlowInEasing
//            0.5f at 225 with LinearOutSlowInEasing
//            1.01f at 322 with LinearOutSlowInEasing
//        }
//    )

    ProvideRipple(isBounded = false) {
        IconResource(
            modifier = modifier
                .clickable {
                    playSparkles = !playSparkles
                    onClicked?.invoke()
                }
                .graphicsLayer {
                    scaleX *= sizeCoefficient
                    scaleY *= sizeCoefficient
                },
            imageVector = favoriteIcon,
            tint = favoriteColor
        )
    }
}

@Preview
@Composable
private fun LikeButtonPreview() {
    var isFavorite by rememberMutableState { false }

    LikeButton(isLiked = isFavorite) {
        isFavorite = !isFavorite
    }
}
