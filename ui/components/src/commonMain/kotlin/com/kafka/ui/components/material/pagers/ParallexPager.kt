package com.kafka.ui.components.material.pagers

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kafka.data.entities.Item
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.isDark
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParallaxPager(
    carouselItems: List<Item>,
    images: List<String>,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    beyondViewportPageCount: Int = 1,
    content: (@Composable (Item, String, Float) -> Unit)? = null
) {
    val pagerState = rememberPagerState(pageCount = { carouselItems.size })
    val isDark = LocalTheme.current.isDark()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background Pager with Parallax Effect
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = beyondViewportPageCount
        ) { page ->
            val item = carouselItems[page]
            val image = images.getOrNull(page) ?: item.coverImage
            val pageOffset = calculatePageOffset(pagerState, page)

//            DynamicTheme(model = image, useDarkTheme = isDark) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(0.66f)
                        .combinedClickable(
                            onClick = { onClick(item.itemId) },
                            onLongClick = { onLongClick(item.itemId) }
                        )
                ) {
                    // Background Image with Parallax
                    AsyncImage(
                        model = image,
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val pageOffset = calculatePageOffset(pagerState, page)

                                // Parallax effect
                                translationX = size.width * pageOffset * -0.2f // Subtle horizontal movement
                                alpha = 1f - pageOffset.absoluteValue * 0.5f // Fade out as page moves away

                                // Scale effect
                                val scale = 1f - pageOffset.absoluteValue * 0.2f
                                scaleX = scale
                                scaleY = scale

                                // Rotation for depth effect
                                rotationY = pageOffset * 10f // Subtle rotation

                                // Interpolate for smoother animation
                                val interpolated = FastOutLinearInEasing.transform(pageOffset.absoluteValue)

                                // Apply transformations based on direction
                                if (pageOffset < 0) {
                                    // Moving right
                                    translationX = -size.width * interpolated * 0.2f
                                } else {
                                    // Moving left
                                    translationX = size.width * interpolated * 0.2f
                                }
                            }
                    )

                    // Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.7f),
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Black.copy(alpha = 0.6f)
                                    )
                                )
                            )
                    )

                    if (content != null) {
                        content(item, image.orEmpty(), pageOffset)
                    } else {
                        // Default Content
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .graphicsLayer {
                                        alpha = 1f - pageOffset.absoluteValue
                                        translationX = size.width * pageOffset * 0.25f
                                    }
                                    .padding(bottom = 16.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.formattedDescription,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    minLines = 3,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .graphicsLayer {
                                            alpha = 1f - pageOffset.absoluteValue
                                            translationX = size.width * pageOffset * 0.5f
                                        }
                                )
                            }
                        }
                    }
                }
//            }
        }

        // Page Indicator
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(carouselItems.size) { iteration ->
                val alpha = if (pagerState.currentPage == iteration) 1f else 0.5f
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .alpha(alpha)
                        .background(Color.White, MaterialTheme.shapes.small)
                )
            }
        }
    }
}

private fun calculatePageOffset(state: PagerState, page: Int): Float {
    return ((state.currentPage - page) + state.currentPageOffsetFraction)
        .coerceIn(-1f, 1f)
}
