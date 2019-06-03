package com.kafka.data.extensions

import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

internal fun <T> Call<T>.fetchBody(): T = execute().bodyOrThrow()

internal fun <T> Response<T>.bodyOrThrow(): T {
    if (!isSuccessful) throw HttpException(this)
    return body()!!
}

internal fun <T> Response<T>.toException() = HttpException(this)

/**
 * executes a call with given number of retries.
 * it returns the response with an exponential backoff.
 *
 * @param firstDelay initial delay; optional
 * @param maxAttempts maximum number of retries; optional
 * @param shouldRetry denotes if it should retry; defaults to [defaultShouldRetry]
 */
internal suspend inline fun <T> Call<T>.executeWithRetry(
    firstDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Exception) -> Boolean = ::defaultShouldRetry
): Response<T> {
    var nextDelay = firstDelay
    repeat(maxAttempts - 1) { attempt ->
        try {
            // Clone a new ready call if needed
            val call = if (isExecuted) clone() else this
            return call.execute()
        } catch (e: Exception) {
            // The response failed, so lets see if we should retry again
            if (attempt == (maxAttempts - 1) || !shouldRetry(e)) {
                throw e
            }
        }
        // Delay to implement exp. backoff
        delay(nextDelay)
        // Increase the next delay
        nextDelay *= 2
    }

    // We should never hit here; it might be because of Charles
    throw IllegalStateException("Unknown exception from executeWithRetry")
}

internal suspend inline fun <T> Call<T>.fetchBodyWithRetry(
    firstDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Exception) -> Boolean = ::defaultShouldRetry
) = executeWithRetry(firstDelay, maxAttempts, shouldRetry).bodyOrThrow()

internal fun defaultShouldRetry(exception: Exception) = when (exception) {
    is HttpException -> exception.code() == 429
    is IOException -> true
    else -> false
}
