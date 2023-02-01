package org.kafka.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kafka.data.feature.item.ItemWithDownload
import org.kafka.common.UiMessage
import org.kafka.common.plus
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.favorites.downloads.DownloadItem
import org.kafka.ui.components.bottomScaffoldPadding
import org.kafka.ui.components.item.LayoutType
import ui.common.theme.theme.Dimens

@Composable
internal fun Downloads(
    items: List<ItemWithDownload>,
    layoutType: LayoutType,
    changeLayoutType: (LayoutType) -> Unit,
    openItemDetail: (String) -> Unit
) {
    if (items.isEmpty()) {
        FullScreenMessage(
            uiMessage = UiMessage(
                title = stringResource(id = R.string.no_downloads_items_title),
                message = stringResource(id = R.string.no_downloads_items_message)
            )
        )
    } else {
        when (layoutType) {
            LayoutType.List -> DownloadsList(
                items = items,
                openItemDetail = openItemDetail,
                header = {
                    LayoutType(layoutType = layoutType, changeViewType = changeLayoutType)
                }
            )

            LayoutType.Grid -> DownloadsGrid(
                favoriteItems = items,
                openItemDetail = openItemDetail,
                header = {
                    LayoutType(layoutType = layoutType, changeViewType = changeLayoutType)
                }
            )
        }
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
private fun DownloadsGrid(
    favoriteItems: List<ItemWithDownload>,
    openItemDetail: (String) -> Unit,
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val padding = PaddingValues(Dimens.Spacing08) +
            PaddingValues(bottom = bottomScaffoldPadding())

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(LayoutType.values().size),
        contentPadding = padding
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            header()
        }

        items(favoriteItems, key = { it.item.itemId }) {
            LibraryItem(item = it.item, openItemDetail = openItemDetail)
        }
    }
}
