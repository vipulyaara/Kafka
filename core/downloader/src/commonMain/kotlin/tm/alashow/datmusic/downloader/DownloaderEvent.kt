/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import tm.alashow.datmusic.downloader.DownloaderEvent.ChooseDownloadsLocation.message

sealed class DownloaderEvent : UiMessageConvertable {
    data object ChooseDownloadsLocation : DownloaderEvent() {
        val message = DownloadMessage.Plain("Downloads location not selected yet")
    }

    data class DownloaderFetchError(val error: Throwable) : DownloaderEvent()

    override fun toUiMessage() = when (this) {
        is ChooseDownloadsLocation -> message
        is DownloaderFetchError -> DownloadMessage.Error(this.error)
    }
}
