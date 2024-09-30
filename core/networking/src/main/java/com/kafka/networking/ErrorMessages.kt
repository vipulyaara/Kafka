package com.kafka.networking

import io.ktor.client.plugins.ServerResponseException
import java.io.InterruptedIOException
import java.net.ProtocolException
import java.net.SocketException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import java.nio.channels.ClosedChannelException
import javax.net.ssl.SSLException

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
    return this is SocketException || this is ClosedChannelException ||
            this is InterruptedIOException || this is ProtocolException ||
            this is SSLException || this is UnknownHostException ||
            this is UnknownServiceException
}
