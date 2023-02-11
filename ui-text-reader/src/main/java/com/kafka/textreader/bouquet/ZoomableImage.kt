package com.kafka.textreader.bouquet

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun ZoomableImage(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    imageAlign: Alignment = Alignment.Center,
    shape: Shape = RectangleShape,
    maxScale: Float = 1f,
    minScale: Float = 3f,
    isRotation: Boolean = false,
    isZoomable: Boolean = true,
    panEnabled: Boolean = true,
    scrollState: ScrollableState? = null,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val scale = remember { mutableStateOf(1f) }
    val rotationState = remember { mutableStateOf(1f) }
    val offsetX = remember { mutableStateOf(1f) }
    val offsetY = remember { mutableStateOf(1f) }
    var panState by rememberSaveable { mutableStateOf(panEnabled) }

    val scaleState by animateFloatAsState(targetValue = scale.value)

    Box(
        modifier = Modifier
            .clip(shape)
            .background(backgroundColor)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { /* NADA :) */ },
                onDoubleClick = {
                    if (scale.value >= 1.5f) {
                        scale.value = 1f
                        offsetX.value = 1f
                        offsetY.value = 1f
                    } else scale.value = 3f
                },
            )
            .pointerInput(Unit) {
                if (isZoomable) {
                    forEachGesture {
                        awaitPointerEventScope {
                            awaitFirstDown()
                            panState = true
                            do {
                                val event = awaitPointerEvent()
                                scale.value *= event.calculateZoom()
                                if (scale.value > 1) {
                                    scrollState?.run {
                                        coroutineScope.launch {
                                            setScrolling(false)
                                        }
                                    }
                                    val offset = event.calculatePan()
                                    offsetX.value += offset.x
                                    offsetY.value += offset.y
                                    rotationState.value += event.calculateRotation()
                                    scrollState?.run {
                                        coroutineScope.launch {
                                            setScrolling(true)
                                        }
                                    }
                                } else {
                                    scale.value = 1f
                                    offsetX.value = 1f
                                    offsetY.value = 1f
                                }
                            } while (event.changes.any { it.pressed })
                        }
                    }
                }
            }
    ) {
        Box(
            modifier = modifier
                .align(imageAlign)
                .graphicsLayer {
                    if (isZoomable) {
                        scaleX = maxOf(maxScale, minOf(minScale, scaleState))
                        scaleY = maxOf(maxScale, minOf(minScale, scaleState))
                        if (isRotation) {
                            rotationZ = rotationState.value
                        }
                        translationX = offsetX.value
                        translationY = offsetY.value
                    }
                }
        ) {
            content()
        }
    }
}

suspend fun ScrollableState.setScrolling(value: Boolean) {
    scroll(scrollPriority = MutatePriority.PreventUserInput) {
        when (value) {
            true -> Unit
            else -> awaitCancellation()
        }
    }
}
