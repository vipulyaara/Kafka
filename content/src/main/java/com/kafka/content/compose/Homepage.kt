package com.kafka.content.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.data.base.extensions.debug
import com.kafka.content.ui.homepage.HomepageViewModel
import com.kafka.content.ui.homepage.HomepageViewState
import com.kafka.content.ui.homepage.bannerImages
import com.kafka.content.ui.query.ArchiveQueryViewModel
import com.kafka.content.ui.query.ArchiveQueryViewState
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemWithRecentItem
import com.kafka.ui.theme.KafkaTheme
import dev.chrisbanes.accompanist.coil.CoilImage

@ExperimentalLazyDsl
@Composable
fun Homepage(actions: Actions) {

    debug { "Homepage loaded" }

    val viewModel: HomepageViewModel = viewModel()
    val queryViewModel: ArchiveQueryViewModel = viewModel()
    val viewState: State<HomepageViewState> = viewModel.stateFlow.collectAsState()
    val queryViewState: State<ArchiveQueryViewState> = queryViewModel.stateFlow.collectAsState()

    remember { viewState.value.tabs?.first { it.isSelected }?.searchQuery?.let { queryViewModel.submitQuery(it) } }

    val items = remember(viewState.value) { queryViewState.value.items.orEmpty() }
    val recentItems = remember(viewState.value) { viewState.value.recentItems.orEmpty() }

    Homepage(items, recentItems, actions.itemDetail)
}

@ExperimentalLazyDsl
@Composable
fun Homepage(items: List<Item>, recentItems: List<ItemWithRecentItem>, onItemClicked: (String) -> Unit) {
    debug { "Drawing homepage" }
    Surface(color = KafkaTheme.colors.surface.copy(alpha = 0.2f)) {
        LazyColumn(content = {
            items(items = bannerImages.take(1)) {
                Banner(it = it)
            }
            items.forEach {
                when {
                    items.indexOf(it) == 0 -> {
                        item { RecentHomepageItems(items = recentItems) }
                    }
                    items.indexOf(it) == 5 -> {
                        item { SuggestedContent(items = items) }
                    }
                    else -> {
                        item { ContentItem(item = it, onItemClick = { onItemClicked(it.itemId) }) }
                    }
                }
            }
        })
    }
}

@Composable
fun Banner(it: Int) {
    Card(modifier = Modifier.height(196.dp).fillMaxWidth().padding(12.dp)) {
        CoilImage(data = it, contentScale = ContentScale.Crop)
    }
}

@Composable
fun RecentHomepageItems(items: List<ItemWithRecentItem>) {
    LazyRowFor(items = items) {
        RecentContentItem(recent = it, onItemClick = {})
    }
}
