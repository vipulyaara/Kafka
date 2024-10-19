/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import com.kafka.data.entities.DownloadRequest
import com.kafka.data.feature.item.DownloadInfo

sealed class DownloadItem(
    open val downloadRequest: DownloadRequest,
    open val downloadInfo: DownloadInfo?,
)

data class FileDownloadItem(
    override val downloadRequest: DownloadRequest,
    override val downloadInfo: DownloadInfo,
) : DownloadItem(downloadRequest, downloadInfo) {
    companion object {
        fun from(downloadRequest: DownloadRequest, downloadInfo: DownloadInfo) =
            FileDownloadItem(downloadRequest, downloadInfo)
    }
}
