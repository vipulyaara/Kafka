package com.kafka.reader.epub

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

@Composable
fun PageTurnHorizontalPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    pageContent: @Composable (Int) -> Unit,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var dragAmount by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var currentPageIndex by remember { mutableStateOf(state.currentPage) }
    var targetPageIndex by remember { mutableStateOf<Int?>(null) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Add logging for state changes
    LaunchedEffect(currentPageIndex) {
        println("LaunchedEffect: Updating PagerState to page $currentPageIndex")
        state.scrollToPage(currentPageIndex, 0f)
    }

    val animatedDragAmount by animateFloatAsState(
        targetValue = dragAmount,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 300f
        ),
        label = "dragAnimation",
        finishedListener = {
            if (!isDragging) {
                println("Animation finished - Resetting states: isDragging=$isDragging, targetPage=$targetPageIndex")
                dragAmount = 0f
                targetPageIndex = null
                isAnimating = false
            }
        }
    )

    val shadowAlpha by animateFloatAsState(
        targetValue = if (isDragging) 0.3f else 0f,
        label = "shadowAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { size = it }
            .pointerInput(currentPageIndex) {
                detectHorizontalDragGestures(
                    onDragStart = { 
                        if (!isAnimating) {
                            println("Drag started - Current: $currentPageIndex, isAnimating: $isAnimating")
                            isDragging = true
                            targetPageIndex = null
                        } else {
                            println("Ignored drag start - Animation in progress")
                        }
                    },
                    onDragEnd = {
                        val threshold = size.width * 0.2f
                        println("Drag ended - Amount: $dragAmount, Threshold: $threshold")
                        
                        coroutineScope.launch {
                            if (abs(dragAmount) > threshold && !isAnimating) {
                                val nextPage = if (dragAmount < 0) {
                                    currentPageIndex + 1
                                } else {
                                    currentPageIndex - 1
                                }.coerceIn(0, state.pageCount - 1)
                                
                                println("Starting page transition: Current=$currentPageIndex -> Target=$nextPage")
                                isAnimating = true
                                targetPageIndex = nextPage
                                
                                dragAmount = if (dragAmount < 0) {
                                    -size.width.toFloat()
                                } else {
                                    size.width.toFloat()
                                }
                                
                                withContext(Dispatchers.Main) {
                                    delay(300)
                                    println("Completing transition to page $nextPage")
                                    currentPageIndex = nextPage
                                    isDragging = false
                                }
                            } else {
                                println("Cancelling transition - Below threshold or animation in progress")
                                dragAmount = 0f
                                isDragging = false
                                targetPageIndex = null
                            }
                        }
                    },
                    onDragCancel = {
                        println("Drag cancelled")
                        isDragging = false
                        dragAmount = 0f
                        targetPageIndex = null
                    },
                    onHorizontalDrag = { change, dragDelta ->
                        if (!isAnimating) {
                            change.consume()
                            val newDragAmount = (dragAmount + dragDelta).coerceIn(
                                -size.width.toFloat(),
                                size.width.toFloat()
                            )
                            if (dragAmount != newDragAmount) {
                                dragAmount = newDragAmount
                                println("Dragging - Amount: $dragAmount, Current: $currentPageIndex, Target: $targetPageIndex")
                            }
                        }
                    }
                )
            }
    ) {
        // First render the background page
        if (dragAmount != 0f || targetPageIndex != null) {
            val backgroundPage = targetPageIndex ?: if (dragAmount < 0) {
                (currentPageIndex + 1).coerceAtMost(state.pageCount - 1)
            } else {
                (currentPageIndex - 1).coerceAtLeast(0)
            }
            
            println("Rendering background page: $backgroundPage (Current: $currentPageIndex, Target: $targetPageIndex)")
            Box(modifier = Modifier.fillMaxSize()) {
                pageContent(backgroundPage)
            }
        }

        // Always render current page, but only animate during drag/transition
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Only apply transformations during drag or animation
                    if (isDragging || isAnimating) {
                        translationX = animatedDragAmount
                        rotationY = (animatedDragAmount / size.width) * -20f
                        shadowElevation = if (isDragging) 8f else 0f
                    }
                }
                .drawWithCache {
                    onDrawBehind {
                        if (isDragging || animatedDragAmount != 0f) {
                            val shadowWidth = 50.dp.toPx()
                            val gradientColors = listOf(
                                Color.Black.copy(alpha = shadowAlpha),
                                Color.Transparent
                            )

                            if (dragAmount > 0) {
                                drawRect(
                                    brush = Brush.horizontalGradient(
                                        colors = gradientColors,
                                        startX = 0f,
                                        endX = shadowWidth
                                    )
                                )
                            } else if (dragAmount < 0) {
                                drawRect(
                                    brush = Brush.horizontalGradient(
                                        colors = gradientColors.reversed(),
                                        startX = size.width.toFloat() - shadowWidth,
                                        endX = size.width.toFloat()
                                    )
                                )
                            }
                        }
                    }
                }
        ) {
            pageContent(currentPageIndex)
        }
    }
}
