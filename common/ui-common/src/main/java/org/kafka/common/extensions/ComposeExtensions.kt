package org.kafka.common.extensions

import androidx.compose.animation.*
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

fun TextStyle.alignCenter() = merge(TextStyle(textAlign = TextAlign.Center))

operator fun TextUnit.plus(other: TextUnit) = (value + other.value).sp

operator fun TextUnit.minus(other: TextUnit) = (value - other.value).sp

@Composable
inline fun <T> rememberMutableState(key: Any? = null, init: @DisallowComposableCalls () -> T) =
    remember(key) { mutableStateOf(init()) }

@Composable
inline fun <T> rememberSavableMutableState(
    key: Any? = null,
    saver: Saver<T, out Any> = autoSaver(),
    crossinline init: @DisallowComposableCalls () -> T,
) = rememberSaveable(key, saver) { mutableStateOf(init()) }

@Composable
fun ProvideContentAlpha(alpha: Float, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentAlpha provides alpha, content = content)
}

@Composable
fun AnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    androidx.compose.animation.AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = enter,
        exit = exit,
        content = content
    )
}

@Composable
fun <T> Flow<T>.rememberAndCollectAsState(
    initial: T,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): T {
    val result by remember(lifecycle) {
        flowWithLifecycle(lifecycle = lifecycle, minActiveState = minActiveState)
    }.collectAsState(initial = initial)
    return result
}

@Composable
fun <T> rememberFlowWithLifecycle(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = remember(flow, lifecycle) {
    flow.flowWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = minActiveState
    )
}

@Composable
fun <T> rememberStateWithLifecycle(flow: Flow<T>, default: T) =
    rememberFlowWithLifecycle(flow).collectAsState(default)

@Composable
fun <T> rememberStateWithLifecycle(
    stateFlow: StateFlow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): State<T> {
    val initialValue = remember(stateFlow) { stateFlow.value }
    return produceState(
        key1 = stateFlow, key2 = lifecycle, key3 = minActiveState,
        initialValue = initialValue
    ) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            stateFlow.collect {
                this@produceState.value = it
            }
        }
    }
}

@Composable
fun <T> CollectEvent(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: (T) -> Unit
): Unit = LaunchedEffect(lifecycle, flow) {
    lifecycle.repeatOnLifecycle(minActiveState) {
        flow.collect {
            collector(it)
        }
    }
}

@Composable
fun WithHighAlpha(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
        content()
    }
}

@Composable
fun WithMediumAlpha(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        content()
    }
}

@Composable
fun WithDisabledAlpha(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
        content()
    }
}

@Composable
fun NoScrollEffect(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalOverScrollConfiguration provides null) {
        content()
    }
}
