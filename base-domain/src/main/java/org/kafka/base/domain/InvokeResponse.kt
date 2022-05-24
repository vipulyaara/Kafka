package org.kafka.base.domain

import com.dropbox.android.external.store4.StoreResponse
import com.dropbox.android.external.store4.doThrow
import org.kafka.base.domain.InvokeResponse.*
import timber.log.Timber

sealed class InvokeResponse<out T> {
    abstract val origin: ResponseOrigin

    data class Loading(override val origin: ResponseOrigin) : InvokeResponse<Nothing>()

    data class Data<T>(val value: T, override val origin: ResponseOrigin) : InvokeResponse<T>()

    data class NoNewData(override val origin: ResponseOrigin) : InvokeResponse<Nothing>()

    sealed class Error : InvokeResponse<Nothing>() {
        data class Exception(
            val error: Throwable,
            override val origin: ResponseOrigin
        ) : Error()

        data class Message(
            val message: String,
            override val origin: ResponseOrigin
        ) : Error()
    }

    fun isLoading() = this is Loading

    fun requireData(): T {
        return when (this) {
            is Data -> value
            is Error -> this.doThrow()
            else -> throw NullPointerException("there is no data in $this")
        }
    }

    fun throwIfError() {
        if (this is StoreResponse.Error) {
            this.doThrow()
        }
    }

    fun errorMessageOrNull(): String? {
        return when (this) {
            is Error.Message -> message
            is Error.Exception -> error.localizedMessage ?: "exception: ${error.javaClass}"
            else -> null
        }
    }

    fun errorMessageOrEmpty() = errorMessageOrNull().orEmpty()

    fun dataOrNull(): T? = when (this) {
        is Data -> value
        else -> null
    }

    fun ifData(block: () -> Unit) {
        if (this is Data) {
            block()
        }
    }

    fun ifError(block: () -> Unit) {
        if (this is Error) {
            block()
        }
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <R> swapType(): InvokeResponse<R> = when (this) {
        is Error -> this
        is Loading -> this
        is NoNewData -> this
        is Data -> throw RuntimeException("cannot swap type for StoreResponse.Data")
    }
}

fun <T> InvokeResponse<T?>.mapIfData(map: (T) -> T): InvokeResponse<T?> {
    return dataOrNull()?.let { it: T ->
        Data(map(it), origin)
    } ?: this
}

enum class ResponseOrigin {
    Cache,
    SourceOfTruth,
    Fetcher
}

fun <T> StoreResponse<T>.printIfError() {
    if (this is StoreResponse.Error.Exception) {
        Timber.e(this.error, error.localizedMessage.orEmpty())
    }
}

fun <T> StoreResponse<T>.asInvokeResponse() = when (this) {
    is StoreResponse.Loading -> Loading(origin.asResponseOrigin())
    is StoreResponse.Data -> Data(dataOrNull(), origin.asResponseOrigin())
    is StoreResponse.Error.Exception -> Error.Exception(error, origin.asResponseOrigin())
    is StoreResponse.Error.Message -> Error.Message(message, origin.asResponseOrigin())
    is StoreResponse.NoNewData -> NoNewData(origin.asResponseOrigin())
}

fun com.dropbox.android.external.store4.ResponseOrigin.asResponseOrigin() = when (this) {
    com.dropbox.android.external.store4.ResponseOrigin.Cache -> ResponseOrigin.Cache
    com.dropbox.android.external.store4.ResponseOrigin.SourceOfTruth -> ResponseOrigin.SourceOfTruth
    com.dropbox.android.external.store4.ResponseOrigin.Fetcher -> ResponseOrigin.Fetcher
}


fun Error.doThrow(): Nothing = when (this) {
    is Error.Exception -> throw error
    is Error.Message -> throw RuntimeException(message)
}
