/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
@file:Suppress("FunctionName")

package tm.alashow.datmusic.downloader

import com.kafka.data.feature.item.DownloadStatus

val DownloadsUnknownError = DownloadMessage.Plain("Unknown error")
val DownloadsFolderNotFound =
    DownloadMessage.Plain("Selected downloads folder not found")
val AudioDownloadErrorFileCreate =
    DownloadMessage.Plain("Couldn't create file in downloads location")
val AudioDownloadErrorInvalidUrl =
    DownloadMessage.Plain("Download has invalid url")

val AudioDownloadResumedExisting =
    DownloadMessage.Plain("Resuming existing download")
val AudioDownloadAlreadyQueued =
    DownloadMessage.Plain("Item already queued for download")

fun AudioDownloadExistingUnknownStatus(status: DownloadStatus) =
    DownloadMessage.Plain("Download request exists but has unhandled status: $status")
