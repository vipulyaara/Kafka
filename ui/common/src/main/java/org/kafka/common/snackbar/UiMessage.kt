package org.kafka.common.snackbar

import android.content.Context
import com.kafka.networking.localizedMessage

sealed class UiMessage {
    data class Plain(val value: String) : UiMessage()
    data class Resource(val value: Int, val formatArgs: List<Any> = emptyList()) : UiMessage()
    data class Error(val value: Throwable) : UiMessage()

    companion object {
        operator fun invoke(value: String) = Plain(value)
        operator fun invoke(value: Int) = Resource(value)
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

fun UiMessage.asString(context: Context): String = when (this) {
    is UiMessage.Plain -> value
    is UiMessage.Resource -> context.getString(value, *formatArgs.toTypedArray())
    is UiMessage.Error -> value.localizedMessage()
}

