package com.kafka.ui.components.material

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/**
 * Possible values of [BottomBackdropState].
 */
enum class BottomBackdropValue {
    /**
     * Indicates the back layer is concealed and the front layer is active.
     */
    Concealed,

    /**
     * Indicates the back layer is revealed and the front layer is inactive.
     */
    Revealed
}

/**
 * State of the [BottomBackdropScaffold] composable.
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmValueChange Optional callback invoked to confirm or veto a pending state change.
 */
@Stable
class BottomBackdropState(
    initialValue: BottomBackdropValue,
    private val animationSpec: AnimationSpec<Float> = BottomBackdropDefaults.AnimationSpec,
    val confirmValueChange: (BottomBackdropValue) -> Boolean = { true }
) {
    var currentValue by mutableStateOf(initialValue)
        private set

    var targetValue by mutableStateOf(initialValue)
        private set

    /**
     * Whether the back layer is revealed.
     */
    val isRevealed: Boolean
        get() = currentValue == BottomBackdropValue.Revealed

    /**
     * Whether the back layer is concealed.
     */
    val isConcealed: Boolean
        get() = currentValue == BottomBackdropValue.Concealed

    /**
     * Reveal the back layer with animation and suspend until it if fully revealed or animation
     * has been cancelled. This method will throw [CancellationException] if the animation is
     * interrupted
     */
    suspend fun reveal() {
        if (confirmValueChange(BottomBackdropValue.Revealed)) {
            targetValue = BottomBackdropValue.Revealed
            currentValue = BottomBackdropValue.Revealed
        }
    }

    /**
     * Conceal the back layer with animation and suspend until it if fully concealed or animation
     * has been cancelled. This method will throw [CancellationException] if the animation is
     * interrupted
     */
    suspend fun conceal() {
        if (confirmValueChange(BottomBackdropValue.Concealed)) {
            targetValue = BottomBackdropValue.Concealed
            currentValue = BottomBackdropValue.Concealed
        }
    }
}

/**
 * Create and [remember] a [BottomBackdropState].
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@Composable
fun rememberBottomBackdropState(
    initialValue: BottomBackdropValue = BottomBackdropValue.Concealed,
    animationSpec: AnimationSpec<Float> = BottomBackdropDefaults.AnimationSpec,
    confirmStateChange: (BottomBackdropValue) -> Boolean = { true }
): BottomBackdropState {
    return remember {
        BottomBackdropState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmValueChange = confirmStateChange
        )
    }
}

/**
 * Contains useful defaults for [BottomBackdropScaffold].
 */
object BottomBackdropDefaults {
    /**
     * The default peek height of the front layer.
     */
    val PeekHeight = 56.dp

    /**
     * The default reveal height of the front layer.
     */
    val RevealHeight = 400.dp

    /**
     * The default shape of the front layer.
     */
    val frontLayerShape: Shape
        @Composable
        get() = MaterialTheme.shapes.large

    /**
     * The default elevation of the front layer.
     */
    val FrontLayerElevation = 8.dp

    /**
     * The default animation spec.
     */
    val AnimationSpec = tween<Float>(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
}

/**
 * A scaffold that implements a bottom-revealing backdrop pattern.
 *
 * The back layer is revealed by sliding the front layer up from the bottom.
 * The front layer can be dragged up to reveal the back layer if [gesturesEnabled] is true.
 */
@Composable
fun BottomBackdropScaffold(
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: BottomBackdropState = rememberBottomBackdropState(),
    gesturesEnabled: Boolean = true,
    peekHeight: Dp = BottomBackdropDefaults.PeekHeight,
    backLayerBackgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    frontLayerShape: Shape = BottomBackdropDefaults.frontLayerShape,
    frontLayerElevation: Dp = BottomBackdropDefaults.FrontLayerElevation,
    frontLayerBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    frontLayerContentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    var backLayerHeight by remember { mutableStateOf(0f) }

    val peekPaddingPx = with(density) { peekHeight.toPx() }

    Box(modifier = modifier.fillMaxSize()) {
        // Back layer - only visible when revealed
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    alpha = if (scaffoldState.isRevealed) 1f else 0f
                }
                .onGloballyPositioned { coordinates ->
                    backLayerHeight = coordinates.size.height.toFloat() - peekPaddingPx
                },
            color = backLayerBackgroundColor
        ) {
            backLayerContent()
        }

        // Front layer
        val revealHeightPx = backLayerHeight

        val frontLayerOffset by animateFloatAsState(
            targetValue = if (scaffoldState.isRevealed) {
                -revealHeightPx // Slide up to reveal
            } else {
                0f // Normal position
            },
            animationSpec = BottomBackdropDefaults.AnimationSpec,
            label = "bottom-backdrop-offset"
        )

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: androidx.compose.ui.geometry.Offset,
                    source: NestedScrollSource
                ): androidx.compose.ui.geometry.Offset {
                    return if (scaffoldState.isConcealed) {
                        androidx.compose.ui.geometry.Offset.Zero
                    } else {
                        available
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
                .graphicsLayer {
                    translationY = frontLayerOffset
                }
                .then(
                    if (gesturesEnabled) {
                        Modifier.draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta ->
                                val newOffset = frontLayerOffset + delta
                                val progress = (-newOffset) / revealHeightPx
                                
                                if (progress > 0.5f && !scaffoldState.isRevealed) {
                                    scope.launch { scaffoldState.reveal() }
                                } else if (progress <= 0.5f && scaffoldState.isRevealed) {
                                    scope.launch { scaffoldState.conceal() }
                                }
                            }
                        )
                    } else {
                        Modifier
                    }
                ),
            shape = frontLayerShape,
            shadowElevation = frontLayerElevation,
            color = frontLayerBackgroundColor,
            contentColor = frontLayerContentColor
        ) {
            frontLayerContent()
        }
    }
} 