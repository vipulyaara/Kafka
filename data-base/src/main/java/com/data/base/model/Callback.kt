package com.data.base.model

import com.data.base.extensions.e
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

interface Callback<T> {
    fun onResult(data: T?, exception: Exception? = null)
}

/**
 * Converts a callback into a [suspendCancellableCoroutine]
 * */
suspend fun <T> awaitCallback(block: (Callback<T>) -> Unit): T =
    suspendCancellableCoroutine { cont ->
        block(object : Callback<T> {
            override fun onResult(data: T?, exception: Exception?) {
                if (exception != null) {
                    cont.resumeWithException(exception)
                } else {
                    cont.resume(data!!) { e(it) { "awaitCallback error" } }
                }
            }
        })
    }
