package com.kafka.data.model

import com.data.base.InvokeStatus
import kotlinx.coroutines.flow.*
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

suspend fun Flow<InvokeStatus>.collectInto(counter: ObservableLoadingCounter) = collect {
    when (it) {
        InvokeStatus.InvokeStarted -> counter.addLoader()
        InvokeStatus.InvokeSuccess, is InvokeStatus.InvokeError -> counter.removeLoader()
    }
}
