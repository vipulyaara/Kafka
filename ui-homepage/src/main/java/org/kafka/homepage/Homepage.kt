package org.kafka.homepage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Homepage
import com.kafka.data.entities.Item
import org.kafka.common.asImmutable
import org.kafka.common.logging.LogCompositions
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.homepage.ui.Carousels
import org.kafka.homepage.ui.ContinueReading
import org.kafka.homepage.ui.FeaturedItems
import org.kafka.item.preloadImages
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun Homepage(viewModel: HomepageViewModel = hiltViewModel()) {
    LogCompositions(tag = "Homepage Feed items")

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewState.homepage?.queryItems) {
        preloadImages(context, viewState.homepage?.queryItems)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopBar(viewState.user, viewModel::openLogin, viewModel::openProfile) },
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Box {
                HomepageFeedItems(
                    homepage = viewState.homepage,
                    isLoading = viewState.isLoading,
                    openItemDetail = viewModel::openItemDetail,
                    openRecentItemDetail = viewModel::openRecentItemDetail,
                    removeRecentItem = viewModel::removeRecentItem
                )
                FullScreenMessage(viewState.message, viewState.isFullScreenError)
            }
        }
    }
}

@Composable
private fun HomepageFeedItems(
    homepage: Homepage?,
    isLoading: Boolean,
    openRecentItemDetail: (String) -> Unit,
    openItemDetail: (String) -> Unit,
    removeRecentItem: (String) -> Unit,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumn(state = lazyListState, contentPadding = scaffoldPadding()) {
        item { Carousels() }

        item {
            ContinueReading(
                readingList = homepage?.continueReadingItems.orEmpty().asImmutable(),
                modifier = Modifier.padding(vertical = Dimens.Spacing20),
                openItemDetail = openRecentItemDetail,
                removeRecentItem = removeRecentItem
            )
        }

        itemsIndexed(
            items = homepage?.queryItems.orEmpty(),
            key = { _, item -> item.itemId }
        ) { index, item ->
            Item(
                item = item,
                modifier = Modifier,
                openItemDetail = openItemDetail
            )
            homepage?.followedItems?.let { FeaturedItems(index, it, openItemDetail) }
        }

        item {
            InfiniteProgressBar(show = isLoading)
        }
    }
}

@Composable
private fun FeaturedItems(
    index: Int,
    items: List<Item>,
    openItemDetail: (String) -> Unit
) {
    if (index == FavoriteRowIndex) {
        FeaturedItems(
            items = items.asImmutable(),
            openItemDetail = openItemDetail
        )
    }
}

private const val FavoriteRowIndex = 15
