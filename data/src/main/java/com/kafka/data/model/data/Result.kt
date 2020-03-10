package com.kafka.data.model.data

sealed class Result<out R> {

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is ErrorResult -> "Error[exception=$exception]"
        }
    }
}

data class Success<out T>(val data: T, val responseModified: Boolean = true) : Result<T>() {
    operator fun invoke() = data
}

data class ErrorResult(val exception: Throwable?) : Result<Nothing>()

fun <T> Result<T>.dataOrThrowError(block: (ErrorResult) -> Nothing = { throwError(it.exception) }): T {
    return when (this) {
        is Success -> this.data
        is ErrorResult -> block(this)
    }
}

private fun throwError(throwable: Throwable?): Nothing =
    throw throwable ?: Throwable("Something went wrong")
