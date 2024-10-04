/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import com.kafka.common.snackbar.UiMessage
import java.util.Collections.emptyList

sealed class DownloadMessage<T : Any>(open val value: T) {
    data class Plain(override val value: String) : DownloadMessage<String>(value)
    data class Resource(override val value: Int, val formatArgs: List<Any> = emptyList()) : DownloadMessage<Int>(value)
    data class Error(override val value: Throwable) : DownloadMessage<Throwable>(value)
}

fun <T : Any> DownloadMessage<T>.toUiMessage(): UiMessage = when (this) {
    is DownloadMessage.Plain -> UiMessage.Plain(this.value)
    is DownloadMessage.Resource -> UiMessage.Resource(this.value, this.formatArgs)
    is DownloadMessage.Error -> UiMessage.Error(this.value)
}

interface UiMessageConvertable {
    fun toUiMessage(): DownloadMessage<*>
}
