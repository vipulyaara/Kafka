package com.kafka.ui.components.item

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import ui.common.theme.theme.Dimens

@Composable
fun PerspectiveShape(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
    cornerRadius: Float = 20f
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        // Create points for the trapezoid
        val topLeft = Offset(size.width * 0.05f, 0f)
        val topRight = Offset(size.width * 0.95f, 0f)
        val bottomRight = Offset(size.width, size.height)
        val bottomLeft = Offset(0f, size.height)

        drawPath(
            path = Path().apply {
                // Start from topLeft with rounded corner
                moveTo(topLeft.x + cornerRadius, topLeft.y)

                // Top edge to topRight
                lineTo(topRight.x - cornerRadius, topRight.y)
                arcTo(
                    rect = Rect(
                        topRight.x - cornerRadius,
                        topRight.y,
                        topRight.x + cornerRadius,
                        topRight.y + cornerRadius * 2
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Right edge to bottomRight
                lineTo(bottomRight.x, bottomRight.y - cornerRadius)
                arcTo(
                    rect = Rect(
                        bottomRight.x - cornerRadius * 2,
                        bottomRight.y - cornerRadius * 2,
                        bottomRight.x,
                        bottomRight.y
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Bottom edge to bottomLeft
                lineTo(bottomLeft.x + cornerRadius, bottomLeft.y)
                arcTo(
                    rect = Rect(
                        bottomLeft.x,
                        bottomLeft.y - cornerRadius * 2,
                        bottomLeft.x + cornerRadius * 2,
                        bottomLeft.y
                    ),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Left edge back to start
                lineTo(topLeft.x, topLeft.y + cornerRadius)
                arcTo(
                    rect = Rect(
                        topLeft.x,
                        topLeft.y,
                        topLeft.x + cornerRadius * 2,
                        topLeft.y + cornerRadius * 2
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                close()
            },
            color = color,
            style = Fill
        )
    }
}

// For gradient version
@Composable
fun PerspectiveShapeWithShadow(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    cornerRadius: Float = 20f
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.Spacing96)
    ) {
        val topLeft = Offset(size.width * 0.05f, 0f)
        val topRight = Offset(size.width * 0.95f, 0f)
        val bottomRight = Offset(size.width, size.height)
        val bottomLeft = Offset(0f, size.height)

        drawPath(
            path = Path().apply {
                // Start from topLeft with rounded corner
                moveTo(topLeft.x + cornerRadius, topLeft.y)

                // Top edge to topRight
                lineTo(topRight.x - cornerRadius, topRight.y)
                arcTo(
                    rect = Rect(
                        topRight.x - cornerRadius,
                        topRight.y,
                        topRight.x + cornerRadius,
                        topRight.y + cornerRadius * 2
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Right edge to bottomRight
                lineTo(bottomRight.x, bottomRight.y - cornerRadius)
                arcTo(
                    rect = Rect(
                        bottomRight.x - cornerRadius * 2,
                        bottomRight.y - cornerRadius * 2,
                        bottomRight.x,
                        bottomRight.y
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Bottom edge to bottomLeft
                lineTo(bottomLeft.x + cornerRadius, bottomLeft.y)
                arcTo(
                    rect = Rect(
                        bottomLeft.x,
                        bottomLeft.y - cornerRadius * 2,
                        bottomLeft.x + cornerRadius * 2,
                        bottomLeft.y
                    ),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Left edge back to start
                lineTo(topLeft.x, topLeft.y + cornerRadius)
                arcTo(
                    rect = Rect(
                        topLeft.x,
                        topLeft.y,
                        topLeft.x + cornerRadius * 2,
                        topLeft.y + cornerRadius * 2
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                close()
            },
            brush = verticalGradient(colors = listOf(color, color.copy(alpha = 0.7f))),
            style = Fill
        )
    }
}