package com.kafka.user.util

import com.data.base.extensions.debug
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class ObservableLoadingCounter @Inject constructor() {
    private val count = AtomicInteger()
    private val loadingState = ConflatedBroadcastChannel(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.asFlow().map { it > 0 }

    fun addLoader() {
        loadingState.sendBlocking(count.incrementAndGet())
    }

    fun removeLoader() {
        loadingState.sendBlocking(count.decrementAndGet())
    }
}

suspend fun ObservableLoadingCounter.collectFrom(statuses: Flow<com.kafka.domain.InvokeStatus>) {
    statuses.collect {
        if (it == com.kafka.domain.InvokeStarted) {
            debug { "Add loader" }
            addLoader()
        } else if (it == com.kafka.domain.InvokeSuccess || it == com.kafka.domain.InvokeTimeout || it is com.kafka.domain.InvokeError) {
            debug { "Remove loader" }
            removeLoader()
        }
    }
}
