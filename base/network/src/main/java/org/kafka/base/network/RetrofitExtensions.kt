package org.kafka.base.network

import android.accounts.NetworkErrorException
import android.net.http.HttpException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.kafka.base.debug
import org.kafka.base.errorLog

suspend fun <T> resultApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): Result<T> {
    return withContext(dispatcher) {
        try {
            debug { "api call success" }
            Result.success(apiCall.invoke())
        } catch (throwable: Throwable) {
            val errorCode = (throwable as? retrofit2.HttpException)?.code()
            errorLog(NetworkErrorException(throwable)) { "Server Error $errorCode" }
            Result.failure(throwable)
        }
    }
}
