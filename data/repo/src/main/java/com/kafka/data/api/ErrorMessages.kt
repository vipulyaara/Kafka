package com.kafka.data.api

import androidx.annotation.StringRes
import com.kafka.data.R
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ProtocolException
import java.net.SocketException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import java.nio.channels.ClosedChannelException
import javax.net.ssl.SSLException

@StringRes
fun Throwable?.localizedMessage(): Int = when (this) {
    is HttpException -> {
        when (code()) {
            404 -> R.string.error_notFound
            500 -> R.string.error_server
            502 -> R.string.error_keyError
            503 -> R.string.error_unavailable
            403, 401 -> R.string.error_auth
            else -> R.string.error_unknown
        }
    }

    else -> when {
        isNetworkException() -> R.string.error_network
        else -> R.string.error_unknown
    }
}

fun Throwable?.isNetworkException(): Boolean {
    return this is SocketException || this is ClosedChannelException ||
            this is InterruptedIOException || this is ProtocolException ||
            this is SSLException || this is UnknownHostException ||
            this is UnknownServiceException
}
