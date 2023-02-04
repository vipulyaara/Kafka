package com.kafka.data.feature.item

import android.net.Uri
import com.kafka.data.entities.DownloadRequest
import com.kafka.data.entities.File
import com.kafka.data.entities.Item

data class ItemWithDownload(
    val downloadRequest: DownloadRequest,
    val downloadInfo: DownloadInfo,
    val file: File,
    val item: Item
)

data class DownloadInfo(
    val id: Int,
    val progress: Float,
    val status: DownloadStatus,
    val fileUri: Uri,
    val sizeStatus: String?
)

enum class DownloadStatus {
    QUEUED,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    CANCELLED,
    FAILED,
    REMOVED,
    DELETED,
    UNKNOWN;

    fun isDownloading() = this == DOWNLOADING

    fun isActive() = this == DOWNLOADING || this == PAUSED

    fun isCompleted() = this == COMPLETED

    fun isPaused() = this == PAUSED
}

fun DownloadStatus.toMessage() = when (this) {
    DownloadStatus.QUEUED -> "Queued"
    DownloadStatus.DOWNLOADING -> "Downloading"
    DownloadStatus.PAUSED -> "Paused"
    DownloadStatus.COMPLETED -> ""
    DownloadStatus.CANCELLED -> "Cancelled"
    DownloadStatus.FAILED -> "Failed"
    DownloadStatus.REMOVED -> "Removed"
    DownloadStatus.DELETED -> "Deleted"
    else -> "Unknown"
}
