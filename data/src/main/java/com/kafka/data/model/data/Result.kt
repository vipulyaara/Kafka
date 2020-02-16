package com.kafka.data.model.data

sealed class Result<out R> {

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is ErrorResult -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

data class Success<out T>(val data: T, val responseModified: Boolean = true) : Result<T>() {
    operator fun invoke() = data
}

data class ErrorResult(val exception: Throwable?) : Result<Nothing>()

object Loading : Result<Nothing>()

fun throwError(message: () -> String) {
    throw RuntimeException(message())
}

