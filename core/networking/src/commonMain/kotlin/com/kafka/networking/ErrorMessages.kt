package com.kafka.networking

import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException

fun Throwable?.localizedMessage(): String = when (this) {
    is ServerResponseException -> {
        when (response.status.value) {
            404 -> "Resource could not be found"
            500 -> "Server could not be reached"
            502 -> "The server is not responding"
            503 -> "The server is unavailable"
            403, 401 -> "Authorization error"
            else -> "Unknown error"
        }
    }

    else -> when {
        isNetworkException() -> "Network error"
        else -> "Unknown error"
    }
}

fun Throwable?.isNetworkException(): Boolean {
    return this is ResponseException || this is RedirectResponseException ||
            this is ServerResponseException ||
            this is HttpRequestTimeoutException || this is IOException
}
