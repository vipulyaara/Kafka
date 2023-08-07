package com.kafka.data.feature.item

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.kafka.data.entities.DownloadRequest
import com.kafka.data.entities.File
import com.kafka.data.entities.Item

@Immutable
data class ItemWithDownload(
    val downloadRequest: DownloadRequest,
    val downloadInfo: DownloadInfo,
    val file: File,
    val item: Item
)

@Immutable
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

    fun isActive() = this == DOWNLOADING || this == PAUSED
}
