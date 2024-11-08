package com.kafka.ui.components.item

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill

// For gradient version
@Composable
fun PerspectiveShapeWithShadow(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    cornerRadius: Float = 10f
) {
    Canvas(modifier = modifier) {
        val topLeft = Offset(size.width * 0.07f, 0f)
        val topRight = Offset(size.width * 0.93f, 0f)
        val bottomRight = Offset(size.width, size.height)
        val bottomLeft = Offset(0f, size.height)

        drawPath(
            path = Path().apply {
                // Start from topLeft with rounded corner
                moveTo(x = topLeft.x + cornerRadius, y = topLeft.y)

                // Top edge to topRight
                lineTo(x = topRight.x - cornerRadius, y = topRight.y)
                arcTo(
                    rect = Rect(
                        left = topRight.x - cornerRadius,
                        top = topRight.y,
                        right = topRight.x + cornerRadius,
                        bottom = topRight.y + cornerRadius * 2
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Right edge to bottomRight
                lineTo(x = bottomRight.x, y = bottomRight.y - cornerRadius)
                arcTo(
                    rect = Rect(
                        left = bottomRight.x - cornerRadius * 2,
                        top = bottomRight.y - cornerRadius * 2,
                        right = bottomRight.x,
                        bottom = bottomRight.y
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Bottom edge to bottomLeft
                lineTo(x = bottomLeft.x + cornerRadius, y = bottomLeft.y)
                arcTo(
                    rect = Rect(
                        left = bottomLeft.x,
                        top = bottomLeft.y - cornerRadius * 2,
                        right = bottomLeft.x + cornerRadius * 2,
                        bottom = bottomLeft.y
                    ),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Left edge back to start
                lineTo(x = topLeft.x, y = topLeft.y + cornerRadius)
                arcTo(
                    rect = Rect(
                        left = topLeft.x,
                        top = topLeft.y,
                        right = topLeft.x + cornerRadius * 2,
                        bottom = topLeft.y + cornerRadius * 2
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                close()
            },
            brush = verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0.7f),
                    color
                )
            ),
            style = Fill
        )
    }
}
