package org.kafka.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Unpublished(
    isUnpublished: Boolean = true,
    content: @Composable () -> Unit
) {
    val color = MaterialTheme.colorScheme.primary

    if (false) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.rotate(-90f)) {
                Text(
                    text = "UNPUBLISHED",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                )
            }

            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(color)
            )
            content()
        }
    } else {
        content()
    }

    if (false) {
        Layout(content = {
            Box(
                Modifier
                    .layoutId("unpublished_label")
                    .vertical()
                    .padding(end = 12.dp)
            ) {
                Text(text = "UNPUBLISHED", style = MaterialTheme.typography.labelSmall)
            }
            Box(
                Modifier
                    .layoutId("divider")
                    .padding(end = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .background(color)
                )
            }
            Box(
                Modifier.layoutId("content")
            ) {
                content()
            }
        }) { measurables, constraints ->
            val labelPlaceable =
                measurables.first { it.layoutId == "unpublished_label" }.measure(constraints)
            val contentPlaceable =
                measurables.first { it.layoutId == "content" }.measure(constraints)

            val layoutHeight = listOf(
                labelPlaceable.width,
                contentPlaceable.height
            ).maxOrNull() ?: 0

            val layoutWidth = constraints.maxWidth

            val dividerPlaceable =
                measurables.first { it.layoutId == "divider" }
                    .measure(constraints)

            layout(layoutWidth, layoutHeight) {
                // navigation icon
                if (true) {
                    labelPlaceable.placeRelative(
                        x = 0,
                        y = (layoutHeight - labelPlaceable.width) / 2
                    )
                }

                dividerPlaceable.placeRelative(
                    x = labelPlaceable.height,
                    y = (layoutHeight - dividerPlaceable.height) / 2
                )

                // action icons
                contentPlaceable.placeRelative(
                    x = labelPlaceable.width + dividerPlaceable.width,
                    y = (layoutHeight - contentPlaceable.height) / 2
                )
            }
        }
    }
}

@Composable
fun DisableUnpublished(content: @Composable () -> Unit) {
    BoxWithConstraints {

    }
}

@Preview
@Composable
private fun Preview() {
    Unpublished {
        Box(
            modifier = Modifier
                .height(12.dp)
                .background(Color.Red)
                .fillMaxWidth()
        )
    }
}

fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }
