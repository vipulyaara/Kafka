/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import org.kafka.downloader.R
import tm.alashow.datmusic.downloader.DownloaderEvent.ChooseDownloadsLocation.message

typealias DownloaderEvents = List<DownloaderEvent>

data class DownloaderEventsError(val events: DownloaderEvents) : Throwable(), UiMessageConvertable {
    override fun toUiMessage() = events.first().toUiMessage()
}

sealed class DownloaderEvent : UiMessageConvertable {
    object ChooseDownloadsLocation : DownloaderEvent() {
        val message = DownloadMessage.Resource(R.string.downloader_enqueue_downloadsLocationNotSelected)
    }

    data class DownloaderFetchError(val error: Throwable) : DownloaderEvent()

    override fun toUiMessage() = when (this) {
        is ChooseDownloadsLocation -> message
        is DownloaderFetchError -> DownloadMessage.Error(this.error)
    }
}
