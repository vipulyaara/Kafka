package com.kafka.ui.components.progress

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class StrokeStyle(
    val width: Dp = 4.dp,
    val strokeCap: StrokeCap = StrokeCap.Round,
    val glowRadius: Dp? = 4.dp
)

@Composable
fun CircularDualIndicator(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    secondColor: Color? = MaterialTheme.colorScheme.secondary,
    tailLength: Float = 140f,
    smoothTransition: Boolean = true,
    strokeStyle: StrokeStyle = StrokeStyle(),
    cycleDuration: Int = 1400,
) {
    val transition = rememberInfiniteTransition()
    val spinAngel by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = cycleDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val tailToDisplay = remember { Animatable(0f) }

    LaunchedEffect(isVisible) {
        val targetTail = if (isVisible) tailLength else 0f
        when {
            smoothTransition -> tailToDisplay.animateTo(
                targetValue = targetTail,
                animationSpec = tween(cycleDuration, easing = LinearEasing)
            )

            else -> tailToDisplay.snapTo(targetTail)
        }
    }

    Canvas(
        modifier = modifier
            // Apply rotation animation
            .rotate(spinAngel)
            // Ensure the CircleLoader maintains a square aspect ratio
            .aspectRatio(1f)
    ) {
        // Iterate over non-null colors
        listOfNotNull(color, secondColor).forEachIndexed { index, color ->
            // If it's not a primary color we rotate the canvas for 180 degrees
            rotate(if (index == 0) 0f else 180f) {
                // Create a sweep gradient brush for the loader
                val brush = Brush.sweepGradient(
                    0f to Color.Transparent,
                    tailToDisplay.value / 360f to color,
                    1f to Color.Transparent
                )
                // Set up paint object
                val paint = setupPaint(strokeStyle, brush)

                // Draw the loader tail
                drawIntoCanvas { canvas ->
                    canvas.drawArc(
                        rect = size.toRect(),
                        startAngle = 0f,
                        sweepAngle = tailToDisplay.value,
                        useCenter = false,
                        paint = paint
                    )
                }
            }
        }
    }
}

fun DrawScope.setupPaint(style: StrokeStyle, brush: Brush): Paint {
    val paint = Paint().apply paint@{
        this@paint.isAntiAlias = true
        this@paint.style = PaintingStyle.Stroke
        this@paint.strokeWidth = style.width.toPx()
        this@paint.strokeCap = style.strokeCap

        brush.applyTo(size, this@paint, 1f)
    }

//    style.glowRadius?.let { radius ->
//        paint.asFrameworkPaint().setShadowLayer(
//            /* radius = */ radius.toPx(),
//            /* dx = */ 0f,
//            /* dy = */ 0f,
//            /* shadowColor = */ Color.White
//        )
//    }

    return paint
}
