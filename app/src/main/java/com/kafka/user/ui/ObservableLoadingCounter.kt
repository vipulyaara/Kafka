package com.kafka.user.ui

import com.kafka.data.data.config.logging.Logger
import com.kafka.data.data.interactor.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class ObservableLoadingCounter @Inject constructor(val logger: Logger) {
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

suspend fun ObservableLoadingCounter.collectFrom(statuses: Flow<InvokeStatus>) {
    statuses.collect {
        if (it == InvokeStarted) {
            logger.d("Add loader")
            addLoader()
        } else if (it == InvokeSuccess || it == InvokeTimeout || it is InvokeError) {
            logger.d("Remove loader")
            removeLoader()
        }
    }
}
