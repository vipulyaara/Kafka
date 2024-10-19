package tm.alashow.datmusic.downloader.mapper

import com.kafka.data.feature.item.DownloadInfo
import com.kafka.data.feature.item.DownloadStatus
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Status
import java.util.Locale
import javax.inject.Inject
import kotlin.math.ln
import kotlin.math.pow

class DownloadInfoMapper @Inject constructor() {
    fun map(download: Download): DownloadInfo {
        return DownloadInfo(
            id = download.id,
            progress = download.progress.coerceAtLeast(0) / 100f,
            fileUri = download.fileUri,
            status = toDownloadStatus(download.status),
            sizeStatus = download.fileSizeStatus(),
        )
    }

    fun toDownloadStatus(status: Status) = when (status) {
        Status.ADDED -> DownloadStatus.QUEUED
        Status.QUEUED -> DownloadStatus.QUEUED
        Status.COMPLETED -> DownloadStatus.COMPLETED
        Status.DOWNLOADING -> DownloadStatus.DOWNLOADING
        Status.FAILED -> DownloadStatus.FAILED
        Status.PAUSED -> DownloadStatus.PAUSED
        Status.REMOVED -> DownloadStatus.REMOVED
        Status.DELETED -> DownloadStatus.DELETED
        Status.CANCELLED -> DownloadStatus.CANCELLED
        else -> DownloadStatus.UNKNOWN
    }

    private fun Download.fileSizeStatus() = when (status) {
        Status.DOWNLOADING, Status.PAUSED -> {
            when {
                total > 0 -> downloaded.humanReadableByteCount() + " / " + total.humanReadableByteCount()
                downloaded > 0 -> downloaded.humanReadableByteCount()
                else -> null
            }
        }

        Status.COMPLETED -> total.humanReadableByteCount()
        else -> null
    }

    private fun Long.humanReadableByteCount(si: Boolean = false): String {
        val unit = if (si) 1000 else 1024

        if (this < unit) return "$this B"

        val exp = (ln(this.toDouble()) / ln(unit.toDouble())).toInt()
        val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1].toString() + ""
        return String.format(
            Locale.ENGLISH,
            "%.1f %sB",
            this / unit.toDouble().pow(exp.toDouble()),
            pre,
        )
    }
}
