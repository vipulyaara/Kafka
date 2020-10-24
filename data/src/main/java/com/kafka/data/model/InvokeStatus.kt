package com.kafka.data.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

sealed class InvokeStatus {
    object Loading : InvokeStatus()
    object Success : InvokeStatus()
    data class Error(val throwable: Throwable) : InvokeStatus()

    fun throwIfError() {
        if (this is Error) {
            throw this.throwable
        }
    }
}

suspend fun Flow<InvokeStatus>.onError(block: (Throwable) -> Unit) = collect { status ->
    if (status is InvokeStatus.Error) {
        block(status.throwable)
    }
}
