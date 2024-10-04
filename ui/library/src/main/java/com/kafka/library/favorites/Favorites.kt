package com.kafka.library.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Item
import com.kafka.common.adaptive.fullSpanItem
import com.kafka.common.adaptive.fullSpanItems
import com.kafka.common.adaptive.useWideLayout
import com.kafka.common.adaptive.windowWidthSizeClass
import com.kafka.common.image.Icons
import com.kafka.common.plus
import com.kafka.common.snackbar.UiMessage
import com.kafka.common.widgets.FullScreenMessage
import com.kafka.favorites.R
import com.kafka.ui.components.MessageBox
import com.kafka.ui.components.bottomScaffoldPadding
import com.kafka.ui.components.item.Item
import com.kafka.ui.components.item.LayoutType
import com.kafka.ui.components.item.LibraryItem
import ui.common.theme.theme.Dimens

@Composable
internal fun Favorites(favoriteViewModel: FavoriteViewModel) {
    val favoriteViewState by favoriteViewModel.state.collectAsStateWithLifecycle()

    Column {
        if (!favoriteViewState.isUserLoggedIn) {
            MessageBox(
                text = stringResource(R.string.log_in_to_sync_favorite),
                trailingIcon = Icons.ArrowForward,
                modifier = Modifier.padding(Dimens.Spacing16),
                onClick = favoriteViewModel::goToLogin
            )
        }

        favoriteViewState.favoriteItems?.let { items ->
            if (items.isEmpty()) {
                FullScreenMessage(UiMessage(stringResource(id = R.string.no_favorites_items_message)))
            } else {
                when (favoriteViewState.layoutType) {
                    LayoutType.List -> FavoriteItemList(
                        favoriteItems = items,
                        openItemDetail = favoriteViewModel::openItemDetail,
                        header = {
                            LayoutType(
                                layoutType = favoriteViewState.layoutType,
                                changeViewType = favoriteViewModel::updateLayoutType
                            )
                        }
                    )

                    LayoutType.Grid -> FavoriteItemGrid(
                        favoriteItems = items,
                        openItemDetail = favoriteViewModel::openItemDetail,
                        header = {
                            LayoutType(
                                layoutType = favoriteViewState.layoutType,
                                changeViewType = favoriteViewModel::updateLayoutType
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteItemList(
    favoriteItems: List<Item>,
    openItemDetail: (String) -> Unit,
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val useWideLayout = windowWidthSizeClass().useWideLayout()
    val padding =
        PaddingValues(Dimens.Spacing08) + PaddingValues(bottom = bottomScaffoldPadding())

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = padding,
    ) {
        fullSpanItem { header() }

        if (useWideLayout) {
            items(items = favoriteItems, key = { item -> item.itemId }) { item ->
                FavoriteItem(item = item, openItemDetail = openItemDetail)
            }
        } else {
            fullSpanItems(items = favoriteItems, key = { item -> item.itemId }) { item ->
                FavoriteItem(item = item, openItemDetail = openItemDetail)
            }
        }
    }
}

@Composable
private fun FavoriteItemGrid(
    favoriteItems: List<Item>,
    openItemDetail: (String) -> Unit,
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val columns = if (windowWidthSizeClass().useWideLayout()) 4 else 2
    val padding =
        PaddingValues(Dimens.Spacing08) + PaddingValues(bottom = bottomScaffoldPadding())

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(columns),
        contentPadding = padding
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            header()
        }

        items(favoriteItems, key = { it.itemId }) {
            LibraryItem(item = it, openItemDetail = openItemDetail)
        }
    }
}

@Composable
private fun FavoriteItem(item: Item, openItemDetail: (String) -> Unit) {
    Item(
        item = item,
        modifier = Modifier
            .clickable { openItemDetail(item.itemId) }
            .padding(vertical = Dimens.Spacing06, horizontal = Dimens.Gutter)
    )
}