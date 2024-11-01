package com.kafka.base.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * Alias to stateIn with defaults
 */
fun <T> Flow<T>.stateInDefault(
    scope: CoroutineScope,
    initialValue: T,
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
) = stateIn(scope, started, initialValue)

suspend fun <T, R> List<T>.mapAsync(
    mapper: suspend (T) -> R,
): List<R> = coroutineScope { map { async { mapper(it) } }.awaitAll() }
