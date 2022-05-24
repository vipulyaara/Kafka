package org.kafka.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kafka.data.entities.Homepage
import org.kafka.common.LogCompositions
import org.kafka.common.asImmutable
import org.kafka.common.extensions.rememberStateWithLifecycle
import org.kafka.common.widgets.DefaultScaffold
import org.kafka.common.widgets.FullScreenMessage
import org.kafka.common.widgets.RekhtaSnackbarHost
import org.kafka.homepage.ui.Carousels
import org.kafka.homepage.ui.ContinueReading
import org.kafka.homepage.ui.HomepageTopBar
import org.kafka.item.Item
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.ui.components.progress.FullScreenProgressBar

@Composable
fun Homepage(viewModel: HomepageViewModel = hiltViewModel()) {
    LogCompositions(tag = "Homepage Feed")

    val viewState by rememberStateWithLifecycle(stateFlow = viewModel.state)
    val navigator = LocalNavigator.current

    Homepage(viewState = viewState)
    FullScreenMessage(message = viewState.message, show = viewState.isFullScreenError)
    FullScreenProgressBar(show = viewState.isFullScreenLoading)
}

@Composable
private fun Homepage(viewState: HomepageViewState) {
    LogCompositions(tag = "Homepage Feed items")

    val lazyListState = rememberLazyListState()
    val snackbarState = SnackbarHostState()
    val navigator = LocalNavigator.current

    DefaultScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomepageTopBar() },
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = { RekhtaSnackbarHost(hostState = snackbarState) },
    ) {
        Box(Modifier.fillMaxSize()) {
            viewState.homepage?.let {
                HomepageFeedItems(
                    homepage = viewState.homepage,
                    lazyListState = lazyListState,
                    openItemDetail = {
                        val url = "https://ia601601.us.archive.org/18/items/in.ernet.dli.2015.68292/2015.68292.Lolita.pdf"
                        navigator.navigate(LeafScreen.Reader.createRoute(url))
//                        navigator.navigate(LeafScreen.ContentDetail.createRoute(it))
                    })
            }

            FullScreenProgressBar(show = viewState.isFullScreenLoading)
            FullScreenMessage(viewState.message, viewState.isFullScreenError)
        }
    }
}

@Composable
private fun HomepageFeedItems(
    homepage: Homepage,
    openItemDetail: (String) -> Unit,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item { Carousels() }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item {
            ContinueReading(
                readingList = homepage.queryItems.asImmutable(),
                openItemDetail = openItemDetail
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        items(homepage.queryItems, key = { it.itemId }) {
            Item(item = it, openItemDetail = openItemDetail)
        }
    }
}
