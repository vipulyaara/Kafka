package org.rekhta.base

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
        throw(it.exception ?: RuntimeException("somethinsg went wrong"))
    }
): T {
    return when {
        this is Success && data != null -> data
        this is ErrorResult -> block(this)
        else -> throw(RuntimeException("something wdent wrong"))
    }
}

fun <T> Result<T?>.dataOrThrow(
    block: (ErrorResult) -> Nothing = {
        throw(it.exception ?: RuntimeException("something wefnt wrong"))
    }
): T {
    return when {
        this is Success && data != null -> data
        this is ErrorResult -> block(this)
        else -> throw(RuntimeException("something wenft wrong"))
    }
}

fun <T, R> Result<T?>.mapData(block: (T) -> R): Result<R> {
    return when {
        this is Success && data != null -> Success(block(data))
        this is ErrorResult && exception != null -> ErrorResult(this.exception)
        else -> throw(RuntimeException("something wenft wrong"))
    }
}

fun <T> Result<T?>.dataOrNull(): T? {
    return when {
        this is Success && data != null -> data
        else -> null
    }
}
