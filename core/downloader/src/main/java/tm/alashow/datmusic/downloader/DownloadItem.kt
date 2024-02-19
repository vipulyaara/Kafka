/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import com.kafka.data.entities.DownloadRequest
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.database.DownloadInfo

sealed class DownloadItem(
    open val downloadRequest: DownloadRequest,
    open val downloadInfo: Download,
)

data class FileDownloadItem(
    override val downloadRequest: DownloadRequest,
    override val downloadInfo: Download,
) : DownloadItem(downloadRequest, downloadInfo) {
    companion object {
        fun from(downloadRequest: DownloadRequest, downloadInfo: Download? = null) =
            FileDownloadItem(downloadRequest, downloadInfo ?: DownloadInfo())
    }
}
