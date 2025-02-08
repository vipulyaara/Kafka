package com.kafka.ui.components.material.pagers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kafka.ui.components.material.offsetForPage

@Composable
fun StackedHorizontalPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    beyondViewportPageCount: Int = 0,
    pageContent: @Composable (Int) -> Unit,
) {
    Column {
        HorizontalPager(
            modifier = modifier,
            state = state,
            pageSpacing = 1.dp,
            beyondViewportPageCount = beyondViewportPageCount
        ) { page ->
            Box(
                modifier = Modifier
                    .zIndex(page * 10f)
                    .graphicsLayer {
                        val startOffset = state.startOffsetForPage(page)
                        translationX = size.width * (startOffset * .99f)

                        alpha = (2f - startOffset) / 2f

                        val scale = 1f - (startOffset * .1f)
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                pageContent(page)
            }
        }
    }
}

// OFFSET ONLY FROM THE LEFT
fun PagerState.startOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtLeast(0f)
}

// OFFSET ONLY FROM THE RIGHT
fun PagerState.endOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtMost(0f)
}