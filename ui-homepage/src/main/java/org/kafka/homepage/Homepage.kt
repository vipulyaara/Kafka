package org.kafka.homepage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Homepage
import org.kafka.common.LogCompositions
import org.kafka.common.asImmutable
import org.kafka.common.extensions.AnimatedVisibility
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.common.widgets.RekhtaSnackbarHost
import org.kafka.homepage.ui.Carousels
import org.kafka.homepage.ui.ContinueReading
import org.kafka.homepage.ui.FeaturedItems
import org.kafka.item.Item
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.ui.components.material.TopBar

@Composable
fun Homepage(viewModel: HomepageViewModel = hiltViewModel()) {
    LogCompositions(tag = "Homepage Feed items")

    val viewState by viewModel.state.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()
    val snackbarState = SnackbarHostState()
    val navigator = LocalNavigator.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar() },
        snackbarHost = { RekhtaSnackbarHost(hostState = snackbarState) },
    ) { padding ->
        Box(Modifier.fillMaxSize()) {
//            FullScreenProgressBar(show = viewState.isFullScreenLoading)

            AnimatedVisibility(viewState.homepage?.queryItems != null) {
                HomepageFeedItems(
                    homepage = viewState.homepage!!,
                    lazyListState = lazyListState,
                    openItemDetail = {
                        navigator.navigate(LeafScreen.ItemDetail.createRoute(it))
                    },
                    paddingValues = padding
                )
            }

            FullScreenMessage(viewState.message, viewState.isFullScreenError)
        }
    }
}

@Composable
private fun HomepageFeedItems(
    homepage: Homepage,
    openItemDetail: (String) -> Unit,
    lazyListState: LazyListState,
    paddingValues: PaddingValues
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize()
    ) {
        item { Carousels() }

        item {
            FeaturedItems(
                readingList = homepage.queryItems.asImmutable(),
                modifier = Modifier.padding(vertical = 12.dp),
                openItemDetail = openItemDetail
            )
        }

        item {
            ContinueReading(
                readingList = homepage.recentItems.asImmutable(),
                modifier = Modifier.padding(vertical = 12.dp),
                openItemDetail = openItemDetail
            )
        }

        items(homepage.queryItems, key = { it.itemId }) {
            Item(item = it, openItemDetail = openItemDetail)
        }
    }
}
