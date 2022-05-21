package org.rekhta.base.domain

sealed class InvokeStatus
object InvokeStarted : InvokeStatus()
object InvokeSuccess : InvokeStatus()
data class InvokeError(val throwable: Throwable) : InvokeStatus()

fun InvokeStatus.errorMessageOrNull(): String? {
    return when (this) {
        is InvokeError -> throwable.stackTraceToString()
        else -> null
    }
}

val InvokeStatus.isLoading
    get() = when (this) {
        is InvokeStarted -> true
        else -> false
    }

fun InvokeStatus.onLoading(block: () -> Unit) {
    if (this is InvokeStarted) block()
}

fun InvokeStatus.onData(block: () -> Unit) {
    if (this is InvokeSuccess) block()
}
