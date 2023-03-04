package org.kafka.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.kafka.common.snackbar.UiMessage
import java.util.UUID

data class UiMessage(
    val message: String = "",
    val title: String = "",
    val id: Long = UUID.randomUUID().mostSignificantBits,
)
//
//fun UiMessage(
//    t: Throwable,
//    id: Long = UUID.randomUUID().mostSignificantBits,
//): UiMessage = UiMessage(
//    message = t.message ?: "Error occurred: $t",
//    id = id,
//)

fun String?.asUiMessage() =
    this?.let { UiMessage(it) } ?: UiMessage("Something went wrong")

class UiMessageManager {
    private val mutex = Mutex()

    private val _messages = MutableStateFlow(emptyList<UiMessage>())

    /**
     * A flow emitting the current message to display.
     */
    val message: Flow<UiMessage?> = _messages.map { it.firstOrNull() }.distinctUntilChanged()

    suspend fun emitMessage(message: UiMessage) {
        mutex.withLock {
            _messages.value = _messages.value + message
        }
    }

    suspend fun clearMessage() {
        mutex.withLock {
            _messages.value = emptyList()
        }
    }
}
