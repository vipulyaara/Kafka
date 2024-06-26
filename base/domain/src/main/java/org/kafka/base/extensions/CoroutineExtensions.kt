package org.kafka.base.extensions

import kotlinx.coroutines.CoroutineScope
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
