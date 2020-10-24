package com.kafka.data.model.model

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

/**
 * @return data if success; throws error or calls the block if error
 * */
fun <T> Result<T>.getOrThrow(
    block: (ErrorResult) -> Nothing = {
        throw(it.exception ?: RuntimeException("something went wrong"))
    }
): T {
    return when (this) {
        is Success -> this.data
        is ErrorResult -> {
            block(this)
        }
    }
}
