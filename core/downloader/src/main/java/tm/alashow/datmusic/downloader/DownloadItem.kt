/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.database.DownloadInfo

sealed class DownloadItem(
    open val downloadInfo: Download
)

data class FileDownloadItem(
    override val downloadInfo: Download
) : DownloadItem(downloadInfo) {
    companion object {
        fun from( downloadInfo: Download? = null) =
            FileDownloadItem(downloadInfo ?: DownloadInfo())
    }
}
