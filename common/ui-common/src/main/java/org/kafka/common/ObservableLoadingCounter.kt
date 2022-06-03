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
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingCounter {
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

    fun toggleLoader(isLoading: Boolean) {
        if (isLoading) {
            addLoader()
        } else {
            removeLoader()
        }
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    uiMessageManager: UiMessageManager? = null,
) = collect { status ->
    debug { "Loading status $status" }
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            Timber.i(status.throwable)
            uiMessageManager?.emitMessage(UiMessage(status.throwable))
            counter.removeLoader()
        }
    }
}
