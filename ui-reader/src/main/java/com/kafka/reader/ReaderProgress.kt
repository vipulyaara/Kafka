package com.kafka.reader

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kafka.data.feature.item.DownloadInfo
import com.kafka.data.feature.item.DownloadStatus
import kotlinx.coroutines.launch
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconResource
import org.kafka.ui.components.rive.RiveAnimation
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.ui.downloader.LocalDownloader
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textSecondary

@Composable
internal fun DownloadProgress(downloadInfo: DownloadInfo) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(Dimens.Spacing24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RiveAnimation(progress = downloadInfo.progress)
            Progress(downloadInfo.progress)
            Spacer(modifier = Modifier.height(Dimens.Spacing24))
            Actions(downloadInfo)
        }
    }
}

@Composable
private fun Actions(downloadInfo: DownloadInfo) {
    val downloader = LocalDownloader.current
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Spacing24),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(modifier = Modifier.weight(0.5f)) {
            if (downloadInfo.sizeStatus != null) {
                Text(
                    text = downloadInfo.sizeStatus!!,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Row(modifier = Modifier.weight(0.5f), horizontalArrangement = Arrangement.SpaceAround) {
            IconResource(
                imageVector = if (downloadInfo.status.isPaused()) Icons.Play else Icons.Pause,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    scope.launch { downloader.togglePlayPause(downloadInfo) }
                }
            )
            IconResource(
                imageVector = Icons.XCircle,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.clickable {
                    scope.launch { downloader.cancel(downloadInfo.id) }
                }
            )
        }
    }
}

suspend fun Downloader.togglePlayPause(downloadInfo: DownloadInfo) {
    if (downloadInfo.status == DownloadStatus.PAUSED) {
        resume(downloadInfo.id)
    } else {
        pause(downloadInfo.id)
    }
}

@Composable
private fun Progress(progress: Float) {
    val progressState by animateFloatAsState(targetValue = progress)

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator(
            progress = progressState,
            modifier = Modifier
                .height(Dimens.Spacing04)
                .weight(1f)
                .clip(RoundedCornerShape(50))
        )
        Text(
            text = "${(progressState * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.textSecondary
        )
    }
}
