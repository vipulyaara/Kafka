package com.kafka.data.extensions

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal suspend fun <A, B> Collection<A>.parallelMap(
    block: suspend (A) -> B
) = coroutineScope {
    map {
        async { block(it) }
    }.map {
        it.await()
    }
}

internal suspend fun <A, B> Collection<A>.parallelForEach(
    block: suspend (A) -> B
) = coroutineScope {
    map {
        async { block(it) }
    }.forEach {
        it.await()
    }
}
