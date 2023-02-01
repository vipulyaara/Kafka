package org.kafka.ui.components.file

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.kafka.data.feature.item.DownloadInfo
import com.kafka.data.feature.item.DownloadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kafka.common.Icons
import org.kafka.common.widgets.IconResource
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.ui.downloader.LocalDownloader
import ui.common.theme.theme.Dimens

@Composable
fun DownloadStatusIcons(
    downloadInfo: DownloadInfo,
    downloader: Downloader = LocalDownloader.current,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val downloadId = downloadInfo.id

    Box(modifier = Modifier.size(Dimens.Spacing44)) {
        IconResource(
            imageVector = downloadInfo.status.icon(),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable {
                    when (downloadInfo.status) {
                        DownloadStatus.DOWNLOADING -> scope.launch { downloader.pause(downloadId) }
                        DownloadStatus.PAUSED -> scope.launch { downloader.resume(downloadId) }
                        DownloadStatus.FAILED,
                        DownloadStatus.CANCELLED -> scope.launch { downloader.retry(downloadId) }

                        else -> {}
                    }
                }
        )

        if (downloadInfo.status == DownloadStatus.DOWNLOADING) {
            val progress by animateFloatAsState(targetValue = downloadInfo.progress)
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round,
                progress = progress
            )
        }

        if (downloadInfo.status == DownloadStatus.QUEUED) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun DownloadStatus.icon() = when (this) {
    DownloadStatus.QUEUED -> Icons.Queue
    DownloadStatus.DOWNLOADING -> Icons.Pause
    DownloadStatus.PAUSED -> Icons.Play
    DownloadStatus.COMPLETED -> Icons.Downloaded
    DownloadStatus.FAILED,
    DownloadStatus.CANCELLED -> Icons.Retry

    else -> Icons.Downloaded
}
