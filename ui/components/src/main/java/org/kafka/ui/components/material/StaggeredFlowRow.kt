package org.kafka.ui.components.material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A staggered flow row that can be put into a scrolling container and can scroll horizontally.
 * */
@Composable
fun StaggeredFlowRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val widths = measurables.map { it.maxIntrinsicWidth(height = constraints.maxHeight) }
        val totalWidth = maxOf(configuration.screenWidthDp.dp.roundToPx(), 2400)
        val horizontalSpacingPx = horizontalSpacing.roundToPx()
        val verticalSpacingPx = verticalSpacing.roundToPx()

        val remainingMeasurables = measurables.toMutableList()
        val remainingWidths = widths.toMutableList()
        val sequences = mutableListOf<List<Placeable>>()
        val crossAxisPositions = mutableListOf<Int>()

        while (remainingMeasurables.isNotEmpty()) {
            var currentWidth = 0
            var index = 0

            while (index < remainingWidths.size && (currentWidth + remainingWidths[index]) <= totalWidth) {
                currentWidth += remainingWidths[index++] + horizontalSpacingPx
            }
            currentWidth -= horizontalSpacingPx

            val placeables = remainingMeasurables
                .zip(other = remainingWidths)
                .take(n = index)
                .map { (measurable, width) ->
                    measurable.measure(
                        constraints = constraints.copy(
                            minWidth = width,
                            maxWidth = width
                        )
                    )
                }

            sequences.add(placeables)
            repeat(times = index) {
                remainingMeasurables.removeFirstOrNull()
                remainingWidths.removeFirstOrNull()
            }

            val crossAxisSpacingToAdd =
                if (remainingMeasurables.isNotEmpty()) verticalSpacingPx else 0
            crossAxisPositions.add(placeables.maxOf { it.height } + crossAxisSpacingToAdd)
        }

        val totalHeight = crossAxisPositions.sum()

        layout(
            width = totalWidth,
            height = totalHeight
        ) {
            var yPosition = 0

            sequences.forEachIndexed { i, sequence ->
                val sequenceMainAxisSizes = sequence.map { it.width }.toIntArray()
                val mainAxisPositions = IntArray(sequenceMainAxisSizes.size) { 0 }

                with(Arrangement.spacedBy(space = horizontalSpacing)) {
                    arrange(
                        totalSize = totalWidth,
                        sizes = sequenceMainAxisSizes,
                        outPositions = mainAxisPositions
                    )
                }

                mainAxisPositions.zip(other = sequence) { xPosition, placeable ->
                    placeable.placeRelative(
                        x = xPosition,
                        y = yPosition
                    )
                }

                yPosition += crossAxisPositions[i]
            }
        }
    }
}
