package com.kafka.reader.epub.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.kafka.reader.epub.settings.ReaderSettings.LineHeight

@Composable
fun LineHeightIcon(variant: LineHeight, modifier: Modifier = Modifier, color: Color = Color.Black) {
    val contentDescription = when (variant) {
        LineHeight.COMPACT -> "Compact line height"
        LineHeight.NORMAL -> "Normal line height"
        LineHeight.RELAXED -> "Relaxed line height"
    }

    Canvas(modifier = modifier, contentDescription = contentDescription) {
        val lineSpacing = when (variant) {
            LineHeight.COMPACT -> 0.17f
            LineHeight.NORMAL -> 0.22f
            LineHeight.RELAXED -> 0.26f
        }

        // Draw horizontal lines
        (0..3).forEach { index ->
            // Calculate total height of all lines and spacing
            val totalHeight = 3 * lineSpacing
            // Find starting Y position to center the lines
            val startY = (size.height - totalHeight * size.height) / 2
            // Draw each line with proper spacing
            val yPosition = startY + (index * lineSpacing * size.height)
            drawLine(
                color = color,
                start = Offset(size.width * 0.3f, yPosition),
                end = Offset(size.width * 0.9f, yPosition),
                strokeWidth = 5f,
                cap = StrokeCap.Round
            )
        }

        // Draw vertical double-headed arrow
        val arrowPath = Path().apply {
            val arrowX = size.width * 0.15f
            val arrowWidth = size.width * 0.08f
            
            // Top arrowhead
            moveTo(arrowX - arrowWidth, size.height * 0.25f)  // Left point
            lineTo(arrowX, size.height * 0.15f)               // Top point
            lineTo(arrowX + arrowWidth, size.height * 0.25f)  // Right point
            
            // Vertical line
            moveTo(arrowX, size.height * 0.15f)
            lineTo(arrowX, size.height * 0.85f)
            
            // Bottom arrowhead
            moveTo(arrowX - arrowWidth, size.height * 0.75f)  // Left point
            lineTo(arrowX, size.height * 0.85f)               // Bottom point
            lineTo(arrowX + arrowWidth, size.height * 0.75f)  // Right point
        }

        drawPath(
            path = arrowPath,
            color = color,
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )
    }
}
