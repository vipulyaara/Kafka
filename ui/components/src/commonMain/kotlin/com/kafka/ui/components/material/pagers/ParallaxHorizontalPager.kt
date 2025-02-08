package com.kafka.ui.components.material.pagers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.kafka.ui.components.material.offsetForPage

@Composable
fun ParallaxHorizontalPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    beyondViewportPageCount: Int = 0,
    pageContent: @Composable (Int) -> Unit,
) {
    HorizontalPager(
        modifier = modifier,
        state = state,
        pageSpacing = 16.dp,
        beyondViewportPageCount = beyondViewportPageCount
    ) { page ->
        Box(
            modifier = Modifier
                .padding(horizontal = 1.dp)
                .drawBehind {
                    drawRect(
                        color = Color.Gray.copy(alpha = 0.2f),
                        size = size.copy(width = 1.dp.toPx())
                    )
                }
                .graphicsLayer {
                    val pageOffset = state.offsetForPage(page)
                    
                    // Calculate different speeds based on whether it's the current page or adjacent
                    val speed = when {
                        page == state.currentPage -> 0.8f  // Current page moves slower
                        page > state.currentPage -> 1.2f   // Next page moves faster
                        else -> 1.2f                       // Previous page moves faster
                    }
                    
                    translationX = size.width * pageOffset * speed
                },
            contentAlignment = Alignment.Center,
        ) {
            pageContent(page)
        }
    }
}
