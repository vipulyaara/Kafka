package com.kafka.data.model

import com.data.base.InvokeStatus
import com.data.base.extensions.debug
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class ObservableLoadingCounter @Inject constructor() {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun addLoader() {
        debug { "add loader $count" }
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        debug { "remove loader $count" }
        loadingState.value = count.decrementAndGet()
    }
}

suspend fun Flow<InvokeStatus>.collectInto(counter: ObservableLoadingCounter) = collect {
    when (it) {
        InvokeStatus.Loading -> counter.addLoader()
        InvokeStatus.Success, is InvokeStatus.Error -> counter.removeLoader()
    }
}
