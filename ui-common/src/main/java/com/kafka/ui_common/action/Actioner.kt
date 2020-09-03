package com.kafka.ui_common.action

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

interface Action

interface Actioner<T: Action> {
    suspend fun sendAction(action: T)
    suspend fun observe(observer: (T) -> Unit)
}

class RealActioner<T: Action> : Actioner<T> {
    private val pendingActions = Channel<T>(BUFFERED)

    override suspend fun sendAction(action: T) {
        if (!pendingActions.isClosedForSend) {
            pendingActions.send(action)
        }
    }

    override suspend fun observe(observer: (T) -> Unit) {
        pendingActions.consumeAsFlow().collect { observer(it) }
    }

}
