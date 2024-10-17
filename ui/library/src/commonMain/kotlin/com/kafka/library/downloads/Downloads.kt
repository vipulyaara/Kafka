package com.kafka.library.downloads

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.plus
import com.kafka.common.snackbar.UiMessage
import com.kafka.common.widgets.FullScreenMessage
import com.kafka.data.feature.item.ItemWithDownload
import com.kafka.ui.components.MessageBox
import com.kafka.ui.components.bottomScaffoldPadding
import kafka.ui.library.generated.resources.Res
import kafka.ui.library.generated.resources.download_storage
import kafka.ui.library.generated.resources.no_downloads_items_message
import org.jetbrains.compose.resources.stringResource
import tm.alashow.datmusic.ui.downloader.LocalDownloader
import ui.common.theme.theme.Dimens

@Composable
internal fun Downloads(downloadsViewModel: DownloadsViewModel) {
    val viewState by downloadsViewModel.state.collectAsStateWithLifecycle()

    viewState.downloadedItems?.let { items ->
        if (items.isEmpty()) {
            FullScreenMessage(UiMessage(stringResource(Res.string.no_downloads_items_message)))
        } else {
            DownloadsList(
                items = items,
                openItemDetail = downloadsViewModel::openItemDetail,
                header = { ChangeDownloadLocation() },
                footer = { DownloadStoragePrompt(Modifier.padding(Dimens.Gutter)) }
            )
        }
    }
}

@Composable
private fun DownloadsList(
    items: List<ItemWithDownload>,
    openItemDetail: (String) -> Unit,
    header: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val padding = PaddingValues(Dimens.Spacing08) +
            PaddingValues(bottom = bottomScaffoldPadding())

    LazyColumn(contentPadding = padding, modifier = modifier.fillMaxSize()) {
        item { header() }

        itemsIndexed(
            items = items,
            key = { _, item -> item.downloadInfo.id }
        ) { _, item ->
            DownloadItem(
                item = item,
                modifier = Modifier.animateItem(),
                openItemDetail = openItemDetail
            )
        }
    }
}

@Composable
private fun ChangeDownloadLocation(modifier: Modifier = Modifier) {
    val downloader = LocalDownloader.current
    val downloadLocation by downloader.downloadLocation.collectAsStateWithLifecycle(null)

    OutlinedButton(
        modifier = modifier,
        onClick = { downloader.requestNewDownloadsLocation() },
        colors = ButtonDefaults.outlinedButtonColors()
    ) {
        Text(text = downloadLocation ?: "...")
    }
}

@Composable
private fun DownloadStoragePrompt(modifier: Modifier = Modifier) {
    MessageBox(text = stringResource(Res.string.download_storage), modifier = modifier)
}
