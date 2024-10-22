package com.kafka.common

import com.kafka.base.ApplicationScope
import com.kafka.common.snackbar.UiMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@ApplicationScope
class UiMessageManager @Inject constructor(){
    private val mutex = Mutex()

    private val _messages = MutableStateFlow(emptyList<UiMessage>())

    /**
     * A flow emitting the current message to display.
     */
    val message: Flow<UiMessage?> = _messages.map { it.firstOrNull() }.distinctUntilChanged()

    suspend fun emitMessage(message: UiMessage) {
        mutex.withLock {
            _messages.value += message
        }
    }

    suspend fun clearMessage() {
        mutex.withLock {
            _messages.value = emptyList()
        }
    }
}
