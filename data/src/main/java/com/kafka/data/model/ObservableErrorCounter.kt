package com.kafka.data.model

import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ObservableErrorCounter @Inject constructor() {
    private val errorState = MutableStateFlow<Throwable?>(null)

    val observable: Flow<Throwable?>
        get() = errorState

    fun sendError(throwable: Throwable?) {
        errorState.value  = throwable
    }
}

suspend fun ObservableErrorCounter.collectFrom(status: Flow<InvokeStatus>) {
    status.catch {
        sendError(it)
    }.map {
        when (it) {
            is InvokeStatus.Error -> it.throwable
            else -> null
        }
    }.collect {
        sendError(it)
    }
}
