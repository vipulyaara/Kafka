package org.kafka.library.downloads

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.feature.item.ItemWithDownload
import org.kafka.common.plus
import org.kafka.common.snackbar.UiMessage
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.favorites.R
import org.kafka.ui.components.bottomScaffoldPadding
import tm.alashow.datmusic.ui.downloader.LocalDownloader
import ui.common.theme.theme.Dimens

@Composable
internal fun Downloads(items: List<ItemWithDownload>, openItemDetail: (String) -> Unit) {
    if (items.isEmpty()) {
        FullScreenMessage(UiMessage(stringResource(id = R.string.no_downloads_items_message)))
    } else {
        DownloadsList(
            items = items,
            openItemDetail = openItemDetail,
            header = { DownloadFolderPrompt() }
        )
    }
}

@Composable
private fun DownloadsList(
    items: List<ItemWithDownload>,
    openItemDetail: (String) -> Unit,
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val padding = PaddingValues(Dimens.Spacing08) +
            PaddingValues(bottom = bottomScaffoldPadding())

    LazyColumn(
        contentPadding = padding,
        modifier = modifier.fillMaxSize()
    ) {
        item { header() }

        itemsIndexed(
            items = items,
            key = { _, item -> item.downloadInfo.id }
        ) { _, item ->
            DownloadItem(item = item, openItemDetail = openItemDetail)
        }
    }
}

@Composable
private fun DownloadFolderPrompt(modifier: Modifier = Modifier) {
    val downloader = LocalDownloader.current
    val downloadLocation by downloader.downloadLocation.collectAsStateWithLifecycle(null)

    OutlinedButton(modifier = modifier, onClick = { downloader.requestNewDownloadsLocation() }) {
        Text(text = downloadLocation ?: "...")
    }
}
