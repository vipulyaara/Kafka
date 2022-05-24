package org.kafka.common

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.kafka.base.extensions.delayFlow
import org.kafka.base.domain.InvokeResponse
import org.kafka.base.domain.InvokeStatus
import org.kafka.base.domain.errorMessageOrNull
import org.threeten.bp.Duration
import javax.inject.Inject

@Immutable
data class UiError(val message: String)

fun String?.asUiError() = this?.let { UiError(it) }

fun UiError(t: Throwable): UiError = UiError(t.message ?: "Error occurred: $t")

fun InvokeStatus.errorOrNull() = errorMessageOrNull()?.let { UiError(it) }
fun <T> InvokeResponse<T>.errorOrNull() = errorMessageOrNull()?.let { UiError(it) }

class SnackbarManager @Inject constructor() {
    private var maxDuration = Duration.ofSeconds(6).toMillis()
    private val maxQueue = 3

    private val pendingErrors = Channel<UiError>(maxQueue, BufferOverflow.DROP_OLDEST)
    private val removeErrorSignal = Channel<Unit>(Channel.RENDEZVOUS)

    /**
     * A flow of [Throwable]s to display in the UI, usually as snackbars. The flow will immediately
     * emit `null`, and will then emit errors sent via [sendError]. Once [maxDuration] has elapsed,
     * or [removeCurrentError] is called (if before that) `null` will be emitted to remove
     * the current error.
     */
    val errors: Flow<UiError?> = flow {
        emit(null)

        pendingErrors.receiveAsFlow().collect {
            emit(it)

            // Wait for either a maxDuration timeout, or a remove signal (whichever comes first)
            merge(
                delayFlow(maxDuration, Unit),
                removeErrorSignal.receiveAsFlow(),
            ).firstOrNull()

            // Remove the error
            emit(null)
        }
    }

    fun launchInScope(
        scope: CoroutineScope,
        onErrorVisibilityChanged: (UiError, Boolean) -> Unit
    ) {
        scope.launch {
            pendingErrors.consumeAsFlow().collect { error ->
                // Set the error
                onErrorVisibilityChanged(error, true)

                merge(
                    delayFlow(Duration.ofSeconds(6).toMillis(), Unit),
                    removeErrorSignal.consumeAsFlow()
                ).firstOrNull()

                // Now remove the error
                onErrorVisibilityChanged(error, false)
                // Delay to allow the current error to disappear
                delay(200)
            }
        }
    }

    /**
     * Add [error] to the queue of errors to display.
     */
    suspend fun sendError(error: UiError) {
        pendingErrors.send(error)
    }

    /**
     * Remove the current error from being displayed.
     */
    suspend fun removeCurrentError() {
        removeErrorSignal.send(Unit)
    }
}
