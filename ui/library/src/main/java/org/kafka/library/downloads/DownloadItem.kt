package org.kafka.library.downloads

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.kafka.data.feature.item.DownloadStatus
import com.kafka.data.feature.item.ItemWithDownload
import org.kafka.common.image.Icons
import org.kafka.ui.components.file.DownloadStatusIcons
import org.kafka.ui.components.item.CoverImage
import org.kafka.ui.components.item.ItemMediaType
import org.kafka.ui.components.item.ItemTitleMedium
import ui.common.theme.theme.Dimens

@Composable
internal fun DownloadItem(
    item: ItemWithDownload,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { openItemDetail(item.item.itemId) }
            .padding(Dimens.Spacing16),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing16)
    ) {
        CoverImage(
            data = item.item.coverImage,
            size = Dimens.CoverSizeMedium,
            placeholder = if (item.item.isAudio) Icons.Audio else Icons.Texts
        )
        DownloadItemDescription(item, Modifier.weight(1f))
        DownloadStatusIcons(downloadInfo = item.downloadInfo)
    }
}

@Composable
fun DownloadItemDescription(item: ItemWithDownload, modifier: Modifier = Modifier) {
    Column(modifier) {
        ItemTitleMedium(item.item.title)
        Spacer(modifier = Modifier.height(Dimens.Spacing02))
        FileName(item.file.name)
        Spacer(modifier = Modifier.height(Dimens.Spacing08))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ItemMediaType(item.item.mediaType)
            Spacer(modifier = Modifier.width(Dimens.Spacing12))

            if (item.downloadInfo.status != DownloadStatus.COMPLETED) {
                val progress by animateFloatAsState(targetValue = item.downloadInfo.progress)
                Progress(progress)
            }
        }
    }
}

@Composable
fun FileName(name: String?, modifier: Modifier = Modifier) {
    Text(
        text = name.orEmpty(),
        style = MaterialTheme.typography.labelMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = modifier
    )
}

@Composable
private fun Progress(progress: Float) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .height(Dimens.Spacing04)
                .weight(1f)
                .clip(RoundedCornerShape(50))
        )
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
