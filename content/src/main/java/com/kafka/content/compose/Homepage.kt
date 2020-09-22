package com.kafka.content.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import com.kafka.ui.theme.KafkaTheme
import dev.chrisbanes.accompanist.coil.CoilImage

@ExperimentalLazyDsl
@Composable
fun Homepage(actions: Actions) {
    val viewModel: HomepageViewModel = viewModel()
    val queryViewModel: ArchiveQueryViewModel = viewModel()
    val viewState: State<HomepageViewState> = viewModel.stateFlow.collectAsState()
    val queryViewState: State<ArchiveQueryViewState> = queryViewModel.stateFlow.collectAsState()

    viewState.value.tabs?.get(1)?.let { viewModel.selectTag(it) }
    viewState.value.tabs?.first { it.isSelected }?.searchQuery?.let { queryViewModel.submitQuery(it) }

    Homepage(items = queryViewState.value.items.orEmpty(), actions.itemDetail)
}

@ExperimentalLazyDsl
@Composable
fun Homepage(items: List<Item>, onItemClicked: (String) -> Unit) {
    debug { "Drawing homepage" }
    Surface(color = KafkaTheme.colors.surface) {
        LazyColumn(content = {
            items(items = bannerImages.take(1)) {
                Banner(it = it)
            }
            items(items = items) {
                ContentItem(item = it, onItemClick = { onItemClicked(it.itemId) })
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
