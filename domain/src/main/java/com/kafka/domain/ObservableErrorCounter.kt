package com.kafka.domain

import com.data.base.InvokeError
import com.data.base.InvokeStatus
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ObservableErrorCounter @Inject constructor() {
    private val errorState = ConflatedBroadcastChannel<Throwable?>()

    val observable: Flow<Throwable?>
        get() = errorState.asFlow()

    fun sendError(throwable: Throwable?) {
        errorState.sendBlocking(throwable)
    }
}

suspend fun ObservableErrorCounter.collectFrom(status: Flow<InvokeStatus>) {
    status.catch {
        sendError(it)
    }.map {
        when (it) {
            is InvokeError -> it.throwable
            else -> null
        }
    }.collect {
        sendError(it)
    }
}
