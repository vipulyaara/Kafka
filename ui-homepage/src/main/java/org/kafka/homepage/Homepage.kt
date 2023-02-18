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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Homepage
import org.kafka.common.asImmutable
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.logging.LogCompositions
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.common.widgets.KafkaSnackbarHost
import org.kafka.homepage.ui.Carousels
import org.kafka.homepage.ui.ContinueReading
import org.kafka.homepage.ui.FeaturedItems
import org.kafka.item.preloadImages
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Screen
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
    val context = LocalContext.current

    LaunchedEffect(viewState.homepage?.queryItems) {
        preloadImages(context, viewState.homepage?.queryItems)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopBar(viewState.user, viewModel::loginClicked, viewModel::openProfile) },
        snackbarHost = { KafkaSnackbarHost(hostState = snackbarState) },
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
    val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()

    Box {
        AnimatedVisibilityFade(viewState.hasQueryItems) {
            HomepageFeedItems(
                homepage = viewState.homepage!!,
                lazyListState = lazyListState,
                openItemDetail = {
                    navigator.navigate(Screen.ItemDetail.createRoute(currentRoot, it))
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
        contentPadding = scaffoldPadding()
    ) {
        item { Carousels() }

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
