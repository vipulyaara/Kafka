package com.airtel.data.model.data

sealed class Result<T> {
    open fun get(): T? = null

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is ErrorResult -> "Error[exception=$exception]"
        }
    }
}

data class Success<T>(val data: T, val responseModified: Boolean = true) : Result<T>() {
    override fun get(): T = data
}

data class ErrorResult<T>(val exception: Exception) : Result<T>()
