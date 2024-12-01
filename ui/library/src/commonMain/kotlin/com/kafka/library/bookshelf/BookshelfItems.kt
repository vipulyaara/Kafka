@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.kafka.library.bookshelf

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kafka.common.adaptive.fullSpanItem
import com.kafka.common.adaptive.fullSpanItems
import com.kafka.common.adaptive.useWideLayout
import com.kafka.common.adaptive.windowWidthSizeClass
import com.kafka.common.animation.LocalAnimatedContentScope
import com.kafka.common.animation.LocalSharedTransitionScope
import com.kafka.common.image.Icons
import com.kafka.common.plus
import com.kafka.common.snackbar.UiMessage
import com.kafka.common.widgets.FullScreenMessage
import com.kafka.data.entities.Bookshelf
import com.kafka.data.entities.BookshelfItem
import com.kafka.navigation.graph.Screen.ItemDetail.Origin
import com.kafka.navigation.graph.Screen.ItemDetail.SharedElementCoverKey
import com.kafka.ui.components.MessageBox
import com.kafka.ui.components.bottomScaffoldPadding
import com.kafka.ui.components.item.GridItem
import com.kafka.ui.components.item.Item
import com.kafka.ui.components.item.LayoutType
import com.kafka.ui.components.progress.InfiniteProgressBar
import kafka.ui.library.generated.resources.Res
import kafka.ui.library.generated.resources.log_in_to_sync_favorite
import kafka.ui.library.generated.resources.no_favorites_items_message
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.inverseOnSurfaceDeep
import ui.common.theme.theme.inverseSurfaceDeep

@Composable
internal fun BookshelfItems(
    bookshelf: Bookshelf,
    detailFactory: (String) -> BookshelfDetailViewModel
) {
    val viewModel = viewModel(key = bookshelf.id) { detailFactory(bookshelf.id) }
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        BookshelfItems(
            state = viewState,
            openItemDetail = viewModel::openItemDetail,
            openLogin = viewModel::openLogin
        )

        InfiniteProgressBar(modifier = Modifier.align(Alignment.Center), show = viewState.loading)

        if (viewModel.isUploads) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = bottomScaffoldPadding())
                    .padding(Dimens.Spacing24),
                containerColor = MaterialTheme.colorScheme.inverseSurfaceDeep,
                shape = CircleShape,
                onClick = { viewModel.chooseFile() }
            ) {
                Icon(
                    imageVector = Icons.Plus,
                    contentDescription = "Upload book",
                    tint = MaterialTheme.colorScheme.inverseOnSurfaceDeep
                )
            }
        }
    }
}


@Composable
internal fun BookshelfItems(
    state: BookshelfDetailState,
    openItemDetail: (String) -> Unit,
    openLogin: () -> Unit
) {
    if (state.items.isEmpty() && !state.loading) {
        FullScreenMessage(UiMessage(stringResource(Res.string.no_favorites_items_message)))
    } else {
        BookshelfItemList(favoriteItems = state.items,
            openItemDetail = openItemDetail,
            header = {
                if (!state.isUserLoggedIn) {
                    MessageBox(
                        text = stringResource(Res.string.log_in_to_sync_favorite),
                        trailingIcon = Icons.ArrowForward,
                        modifier = Modifier.padding(vertical = Dimens.Spacing16),
                        onClick = openLogin
                    )
                }
            })
    }
}

@Composable
private fun BookshelfItemList(
    favoriteItems: List<BookshelfItem>,
    layoutType: LayoutType = LayoutType.Grid,
    openItemDetail: (String) -> Unit,
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val useWideLayout = windowWidthSizeClass().useWideLayout()
    val columns = if (windowWidthSizeClass().useWideLayout()) 4 else 2
    val padding = PaddingValues(Dimens.Spacing12) + PaddingValues(bottom = bottomScaffoldPadding())

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
        contentPadding = padding,
    ) {
        fullSpanItem { header() }

        if (layoutType == LayoutType.List) {
            if (useWideLayout) {
                items(items = favoriteItems, key = { item -> item.itemId }) { item ->
                    FavoriteItem(item = item, openItemDetail = openItemDetail)
                }
            } else {
                fullSpanItems(items = favoriteItems, key = { item -> item.itemId }) { item ->
                    FavoriteItem(item = item, openItemDetail = openItemDetail)
                }
            }
        } else {
            items(items = favoriteItems, key = { it.itemId }) { item ->
                with(LocalSharedTransitionScope.current) {
                    GridItem(mediaType = item.mediaType,
                        coverImage = item.coverImage,
                        modifier = Modifier
                            .clickable { openItemDetail(item.itemId) }
                            .sharedElement(
                                animatedVisibilityScope = LocalAnimatedContentScope.current,
                                state = rememberSharedContentState(
                                    key = SharedElementCoverKey(item.coverImage, Origin.Grid)
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteItem(item: BookshelfItem, openItemDetail: (String) -> Unit) {
    Item(item = item,
        modifier = Modifier.clickable { openItemDetail(item.itemId) }
            .padding(horizontal = Dimens.Gutter))
}
