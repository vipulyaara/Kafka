package org.kafka.base.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit

/**
 * Alias to stateIn with defaults
 */
fun <T> Flow<T>.stateInDefault(
    scope: CoroutineScope,
    initialValue: T,
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
) = stateIn(scope, started, initialValue)

fun flowInterval(interval: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Flow<Int> {
    val delayMillis = timeUnit.toMillis(interval)
    return channelFlow {
        var tick = 0
        send(tick)
        while (true) {
            delay(delayMillis)
            send(++tick)
        }
    }
}
