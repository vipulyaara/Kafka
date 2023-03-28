package org.kafka.homepage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Homepage
import org.kafka.common.asImmutable
import org.kafka.common.logging.LogCompositions
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.homepage.components.Carousels
import org.kafka.homepage.components.ContinueReading
import org.kafka.homepage.components.FavoriteItems
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun Homepage(viewModel: HomepageViewModel = hiltViewModel()) {
    LogCompositions(tag = "Homepage Feed items")

    val viewState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopBar(viewState.user, viewModel::openLogin, viewModel::openProfile) },
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            HomepageFeedItems(
                homepage = viewState.homepage,
                isLoading = viewState.isLoading,
                openItemDetail = viewModel::openItemDetail,
                openRecentItemDetail = viewModel::openRecentItemDetail,
                removeRecentItem = viewModel::removeRecentItem
            )
            FullScreenMessage(
                uiMessage = viewState.message,
                show = viewState.isFullScreenError,
                onRetry = viewModel::retry
            )
        }
    }
}

@Composable
private fun HomepageFeedItems(
    homepage: Homepage,
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
                readingList = homepage.continueReadingItems.asImmutable(),
                modifier = Modifier
                    .padding(vertical = Dimens.Spacing20)
                    .animateItemPlacement(),
                openItemDetail = openRecentItemDetail,
                removeRecentItem = removeRecentItem
            )
        }

        itemsIndexed(
            items = homepage.queryItems,
            key = { _, item -> item.itemId }
        ) { index, item ->
            Item(
                item = item,
                modifier = Modifier,
                openItemDetail = openItemDetail
            )
            if (homepage.hasFavorites) {
                if (index == FavoriteRowIndex) {
                    FavoriteItems(
                        items = homepage.favoriteItems.asImmutable(),
                        openItemDetail = openItemDetail
                    )
                }
            }
        }

        item {
            InfiniteProgressBar(show = isLoading)
        }
    }
}

private const val FavoriteRowIndex = 15
