/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import com.kafka.common.snackbar.UiMessage

sealed class DownloadMessage<T : Any>(open val value: T) {
    data class Plain(override val value: String) : DownloadMessage<String>(value)
    data class Error(override val value: Throwable) : DownloadMessage<Throwable>(value)
}

fun <T : Any> DownloadMessage<T>.toUiMessage(): UiMessage = when (this) {
    is DownloadMessage.Plain -> UiMessage.Plain(this.value)
    is DownloadMessage.Error -> UiMessage.Error(this.value)
}

interface UiMessageConvertable {
    fun toUiMessage(): DownloadMessage<*>
}
