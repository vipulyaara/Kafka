package org.kafka.homepage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Homepage
import org.kafka.common.asImmutable
import org.kafka.common.extensions.AnimatedVisibility
import org.kafka.common.logging.LogCompositions
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.common.widgets.RekhtaSnackbarHost
import org.kafka.homepage.ui.Carousels
import org.kafka.homepage.ui.ContinueReading
import org.kafka.homepage.ui.FeaturedItems
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.RootScreen
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun Homepage(viewModel: HomepageViewModel = hiltViewModel()) {
    LogCompositions(tag = "Homepage Feed items")

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = SnackbarHostState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { },
        snackbarHost = { RekhtaSnackbarHost(hostState = snackbarState) },
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Homepage(viewState) { viewModel.removeRecentItem(it) }
        }
    }
}

@Composable
private fun Homepage(viewState: HomepageViewState, removeRecentItem: (String) -> Unit) {
    val lazyListState = rememberLazyListState()
    val navigator = LocalNavigator.current

    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(viewState.hasQueryItems) {
            HomepageFeedItems(
                homepage = viewState.homepage!!,
                lazyListState = lazyListState,
                openItemDetail = {
                    navigator.navigate(LeafScreen.ItemDetail.buildRoute(it, RootScreen.Home))
                },
                removeRecentItem = removeRecentItem
            )
        }
        InfiniteProgressBar(
            show = viewState.isFullScreenLoading,
            modifier = Modifier.align(Alignment.Center)
        )
        FullScreenMessage(viewState.message, viewState.isFullScreenError)
    }
}

@Composable
private fun HomepageFeedItems(
    homepage: Homepage,
    openItemDetail: (String) -> Unit,
    removeRecentItem: (String) -> Unit,
    lazyListState: LazyListState
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = scaffoldPadding(),
        modifier = Modifier.fillMaxSize()
    ) {
        item { Carousels(modifier = Modifier.padding(top = Dimens.Spacing24)) }

        item {
            ContinueReading(
                readingList = homepage.continueReadingItems.asImmutable(),
                modifier = Modifier.padding(vertical = Dimens.Spacing20),
                openItemDetail = openItemDetail,
                removeRecentItem = removeRecentItem
            )
        }

        itemsIndexed(
            items = homepage.queryItems,
            key = { _, item -> item.itemId }
        ) { index, item ->
            if (index == FavoriteRowIndex) {
                FeaturedItems(
                    items = homepage.followedItems.asImmutable(),
                    openItemDetail = openItemDetail
                )
            } else {
                Item(
                    item = item,
                    modifier = Modifier,
                    openItemDetail = openItemDetail
                )
            }
        }
    }
}

private const val FavoriteRowIndex = 15
