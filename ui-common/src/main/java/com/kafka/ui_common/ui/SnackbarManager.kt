package com.kafka.ui_common.ui

import com.kafka.ui_common.extensions.delayFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import javax.inject.Inject

class SnackbarManager @Inject constructor() {
    // We want a maximum of 3 errors queued
    private val pendingErrors = Channel<String>(3)
    private val removeErrorSignal = Channel<Unit>(1)

    fun launchInScope(
        scope: CoroutineScope,
        onErrorVisibilityChanged: (String, Boolean) -> Unit
    ) {
        scope.launch {
            pendingErrors.consumeAsFlow().collect { error ->
                // Set the error
                onErrorVisibilityChanged(error, true)

                merge(
                    delayFlow(Duration.ofSeconds(6).toMillis(), Unit),
                    removeErrorSignal.receiveAsFlow()
                ).firstOrNull()

                // Now remove the error
                onErrorVisibilityChanged(error, false)
                // Delay to allow the current error to disappear
                delay(200)
            }
        }
    }

    fun sendError(error: String) {
        if (!pendingErrors.isClosedForSend) {
            pendingErrors.offer(error)
        }
    }

    fun removeCurrentError() {
        if (!removeErrorSignal.isClosedForSend) {
            removeErrorSignal.offer(Unit)
        }
    }
}
