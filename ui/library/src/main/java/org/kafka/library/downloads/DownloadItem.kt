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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kafka.data.feature.item.DownloadStatus
import com.kafka.data.feature.item.ItemWithDownload
import org.kafka.ui.components.file.DownloadStatusIcons
import org.kafka.ui.components.item.CoverImage
import org.kafka.ui.components.item.ItemCreator
import org.kafka.ui.components.item.ItemTitleMedium
import org.kafka.ui.components.item.ItemMediaType
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
        CoverImage(item.item.coverImage)
        DownloadItemDescription(item, Modifier.weight(1f))
        DownloadStatusIcons(downloadInfo = item.downloadInfo)
    }
}

@Composable
fun DownloadItemDescription(item: ItemWithDownload, modifier: Modifier = Modifier) {
    Column(modifier) {
        ItemTitleMedium(item.file.name)
        Spacer(modifier = Modifier.height(Dimens.Spacing02))
        ItemCreator(item.item.title)
        Spacer(modifier = Modifier.height(Dimens.Spacing02))
        ItemMediaType(item.item.mediaType)
        Spacer(modifier = Modifier.height(Dimens.Spacing02))

        if (item.downloadInfo.status != DownloadStatus.COMPLETED) {
            val progress by animateFloatAsState(targetValue = item.downloadInfo.progress)
            Progress(progress)
        }
    }
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
