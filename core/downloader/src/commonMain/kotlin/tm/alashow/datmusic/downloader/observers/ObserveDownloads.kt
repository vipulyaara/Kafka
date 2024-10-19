/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader.observers

import kotlinx.coroutines.flow.Flow
import tm.alashow.datmusic.downloader.FileDownloadItem

expect class ObserveDownloads  {
    fun execute(): Flow<DownloadItems>
}

data class DownloadItems(val files: List<FileDownloadItem> = emptyList())
