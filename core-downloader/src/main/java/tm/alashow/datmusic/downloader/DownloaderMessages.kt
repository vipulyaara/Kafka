/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
@file:Suppress("FunctionName")

package tm.alashow.datmusic.downloader

import com.tonyodev.fetch2.Status
import org.kafka.downloader.R

val DownloadsUnknownError = DownloadMessage.Resource(R.string.error_unknown)
val DownloadsFolderNotFound =
    DownloadMessage.Resource(R.string.downloader_enqueue_downloadsNotFound)
val AudioDownloadErrorFileCreate =
    DownloadMessage.Resource(R.string.downloader_enqueue_audio_error_fileCreate)
val AudioDownloadErrorInvalidUrl =
    DownloadMessage.Resource(R.string.downloader_enqueue_audio_error_invalidUrl)

val AudioDownloadQueued = DownloadMessage.Resource(R.string.downloader_enqueue_audio_queued)
val AudioDownloadResumedExisting =
    DownloadMessage.Resource(R.string.downloader_enqueue_audio_existing_resuming)
val AudioDownloadAlreadyQueued =
    DownloadMessage.Resource(R.string.downloader_enqueue_audio_existing_alreadyQueued)
val AudioDownloadAlreadyCompleted =
    DownloadMessage.Resource(R.string.downloader_enqueue_audio_existing_completed)

fun AudioDownloadExistingUnknownStatus(status: Status) =
    DownloadMessage.Resource(R.string.downloader_enqueue_audio_existing_unknown, listOf(status))
