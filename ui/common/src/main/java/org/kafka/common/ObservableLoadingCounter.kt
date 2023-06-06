package org.kafka.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.kafka.base.debug
import org.kafka.base.domain.InvokeError
import org.kafka.base.domain.InvokeStarted
import org.kafka.base.domain.InvokeStatus
import org.kafka.base.domain.InvokeSuccess
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.snackbar.toUiMessage
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class ObservableLoadingCounter @Inject constructor() {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    uiMessageManager: UiMessageManager? = null,
    onStatus: suspend (InvokeStatus) -> Unit = {},
) = collect { status ->
    debug { "Loading status $status" }
    onStatus(status)
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            Timber.i(status.throwable)
            uiMessageManager?.emitMessage(status.throwable.toUiMessage())
            counter.removeLoader()
        }
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    snackbarManager: SnackbarManager? = null,
    onStatus: suspend (InvokeStatus) -> Unit = {},
) = collect { status ->
    debug { "Loading status $status" }
    onStatus(status)
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            Timber.i(status.throwable)
            snackbarManager?.addMessage(status.throwable.toUiMessage())
            counter.removeLoader()
        }
    }
}
