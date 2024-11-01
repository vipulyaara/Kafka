package com.kafka.common

import com.kafka.base.debug
import com.kafka.base.domain.InvokeError
import com.kafka.base.domain.InvokeStarted
import com.kafka.base.domain.InvokeStatus
import com.kafka.base.domain.InvokeSuccess
import com.kafka.base.i
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.toUiMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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
    uiMessageManager: UiMessageManager,
    onStatus: suspend (InvokeStatus) -> Unit = {},
) = collect { status ->
    debug { "Loading status $status" }
    onStatus(status)
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            i(status.throwable) { "Error loading" }
            uiMessageManager.emitMessage(status.throwable.toUiMessage())
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
            i(status.throwable) { "Error loading" }
            snackbarManager?.addMessage(status.throwable.toUiMessage())
            counter.removeLoader()
        }
    }
}
