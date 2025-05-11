package com.kafka.ui.components.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.asString
import com.kafka.common.widgets.LocalSnackbarHostState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun SnackbarMessagesHost(
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current,
    snackbarManager: SnackbarManager,
) {
    val coroutine = rememberCoroutineScope()

    CollectEvent(snackbarManager.messages) {
        coroutine.launch {
            val message = it.message.asString()
            val actionLabel = it.action?.label?.asString()
            when (snackbarHostState.showSnackbar(message, actionLabel)) {
                SnackbarResult.ActionPerformed -> snackbarManager.onMessageActionPerformed(it)
                SnackbarResult.Dismissed -> snackbarManager.onMessageDismissed(it)
            }
        }
    }
}

@Composable
private fun <T> CollectEvent(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: (T) -> Unit,
): Unit = LaunchedEffect(lifecycle, flow) {
    lifecycle.repeatOnLifecycle(minActiveState) {
        flow.collect {
            collector(it)
        }
    }
}
