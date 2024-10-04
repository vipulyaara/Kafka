package com.kafka.common.snackbar

import com.kafka.networking.localizedMessage

sealed class UiMessage {
    data class Plain(val value: String) : UiMessage()
    data class Error(val value: Throwable) : UiMessage()

    companion object {
        operator fun invoke(value: String) = Plain(value)
    }
}

fun Throwable?.toUiMessage() = when {
    else -> when (val message = this.localizedMessage()) {
        "Unknown error" -> UiMessage.Plain(
            this?.message ?: this?.javaClass?.simpleName ?: ""
        )

        else -> UiMessage.Plain(message)
    }
}

fun UiMessage.asString(): String = when (this) {
    is UiMessage.Plain -> value
    is UiMessage.Error -> value.localizedMessage()
}

