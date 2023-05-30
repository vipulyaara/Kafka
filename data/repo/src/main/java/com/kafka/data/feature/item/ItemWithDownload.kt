package com.kafka.data.feature.item

import android.net.Uri
import com.kafka.data.entities.DownloadRequest
import com.kafka.data.entities.File
import com.kafka.data.entities.Item

data class ItemWithDownload(
    val downloadInfo: DownloadInfo,
    val file: File,
    val item: Item
)

data class DownloadInfo(
    val id: Int,
    val progress: Float,
    val status: DownloadStatus,
    val fileUri: Uri,
    val size: String?
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

    fun isActive() = this == DOWNLOADING || this == PAUSED
}
