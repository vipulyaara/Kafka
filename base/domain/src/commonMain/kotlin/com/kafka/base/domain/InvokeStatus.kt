package com.kafka.base.domain

sealed class InvokeStatus
data object InvokeStarted : InvokeStatus()
data object InvokeSuccess : InvokeStatus()
data class InvokeError(val throwable: Throwable) : InvokeStatus()
