package com.kafka.ui.components.material.pagers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import kotlin.math.sqrt

@Composable
fun CircleRevealPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    beyondViewportPageCount: Int = 0,
    pageContent: @Composable (Int) -> Unit,
) {
    var offsetY by remember { mutableStateOf(0f) }

    HorizontalPager(
        state = state,
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        offsetY = event.changes.first().position.y
                    }
                }
            }
            .padding(horizontal = 32.dp, vertical = 64.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color.Black)
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val pageOffset = ((state.currentPage - page) + state.currentPageOffsetFraction)
                        .coerceIn(-1f, 1f)
                    translationX = size.width * pageOffset

                    val endOffset = pageOffset.coerceIn(-1f, 1f)

                    shadowElevation = 20f
                    shape = CirclePath(
                        progress = 1f - endOffset.absoluteValue,
                        origin = Offset(
                            size.width,
                            offsetY,
                        )
                    )
                    clip = true

                    val absoluteOffset = pageOffset.absoluteValue
                    val scale = 1f + (absoluteOffset.absoluteValue * .4f)

                    scaleX = scale
                    scaleY = scale

                    val startOffset = pageOffset.coerceIn(-1f, 1f)
                    alpha = (2f - startOffset) / 2f
                },
            contentAlignment = Alignment.Center,
        ) {
            pageContent(page)
        }
    }
}

class CirclePath(private val progress: Float, private val origin: Offset = Offset(0f, 0f)) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val center = Offset(
            x = size.center.x - ((size.center.x - origin.x) * (1f - progress)),
            y = size.center.y - ((size.center.y - origin.y) * (1f - progress)),
        )
        val radius = (sqrt(
            size.height * size.height + size.width * size.width
        ) * .5f) * progress

        return Outline.Generic(Path().apply {
            addOval(
                Rect(
                    center = center,
                    radius = radius,
                )
            )
        })
    }
}