package com.kafka.item.files

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kafka.data.entities.File
import com.kafka.data.feature.item.DownloadInfo
import kotlinx.coroutines.CoroutineScope
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import com.kafka.common.widgets.IconResource
import com.kafka.item.R
import com.kafka.ui.components.item.DownloadStatusIcons
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.ui.downloader.LocalDownloader
import ui.common.theme.theme.Dimens

@Composable
internal fun FileItem(
    file: File,
    onFileClicked: (File) -> Unit,
    onDownloadClicked: (File) -> Unit,
    downloadInfo: DownloadInfo?,
    modifier: Modifier = Modifier,
    downloader: Downloader = LocalDownloader.current,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val fileSubtitle = if (downloadInfo?.status?.isActive() == true) {
        listOf(file.extension, downloadInfo.sizeStatus).joinToString(" - ")
    } else {
        listOf(file.extension, file.mapSize()).joinToString(" - ")
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onFileClicked(file) }
            .animateContentSize()
            .padding(Dimens.Spacing16),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
        ) {
            Text(
                text = file.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
            )

            Text(
                text = fileSubtitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
            )
        }

        Box {
            if (downloadInfo == null) {
                IconButton(
                    onClickLabel = stringResource(R.string.cd_download_file),
                    onClick = { onDownloadClicked(file) }) {
                    IconResource(
                        imageVector = Icons.Download,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(R.string.cd_download)
                    )
                }
            }

            if (downloadInfo != null) {
                DownloadStatusIcons(
                    downloadInfo = downloadInfo,
                    downloader = downloader,
                    scope = scope
                )
            }
        }
    }
}
