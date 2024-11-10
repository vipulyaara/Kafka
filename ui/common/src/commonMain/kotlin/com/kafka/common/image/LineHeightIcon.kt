import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

enum class LineHeightVariant {
    COMPACT,
    NORMAL,
    RELAXED
}

@Composable
fun LineHeightIcon(
    variant: LineHeightVariant,
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    val contentDescription = when (variant) {
        LineHeightVariant.COMPACT -> "Compact line height"
        LineHeightVariant.NORMAL -> "Normal line height"
        LineHeightVariant.RELAXED -> "Relaxed line height"
    }

    Canvas(
        modifier = modifier.size(24.dp),
        contentDescription = contentDescription
    ) {
        val lineSpacing = when (variant) {
            LineHeightVariant.COMPACT -> 0.2f
            LineHeightVariant.NORMAL -> 0.25f
            LineHeightVariant.RELAXED -> 0.3f
        }

        // Draw horizontal lines
        (0..3).forEach { index ->
            val yPosition = size.height * (0.1f + (index * lineSpacing))
            drawLine(
                color = color,
                start = Offset(size.width * 0.3f, yPosition),
                end = Offset(size.width * 0.9f, yPosition),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
        }

        // Draw arrow
        val arrowPath = Path().apply {
            // Top arrow point
            moveTo(size.width * 0.1f, size.height * 0.25f)
            lineTo(size.width * 0.2f, size.height * 0.5f)
            // Bottom arrow point
            moveTo(size.width * 0.1f, size.height * 0.75f)
            lineTo(size.width * 0.2f, size.height * 0.5f)
        }
        drawPath(
            path = arrowPath,
            color = color,
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )
    }
} 