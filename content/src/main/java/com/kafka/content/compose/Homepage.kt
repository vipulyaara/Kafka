package com.kafka.content.compose

import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.viewinterop.viewModel
import com.kafka.content.ui.homepage.HomepageViewModel
import com.kafka.content.ui.homepage.HomepageViewState
import com.kafka.content.ui.query.ArchiveQueryViewModel
import com.kafka.content.ui.query.ArchiveQueryViewState
import com.kafka.data.entities.Item

@Composable
fun Homepage() {
    val viewModel: HomepageViewModel = viewModel()
    val queryViewModel: ArchiveQueryViewModel = viewModel()
    val viewState: State<HomepageViewState> = viewModel.stateFlow.collectAsState()
    val queryViewState: State<ArchiveQueryViewState> = queryViewModel.stateFlow.collectAsState()

    viewState.value.tabs?.get(0)?.let { viewModel.selectTag(it) }
    viewState.value.tabs?.first { it.isSelected }?.searchQuery?.let { queryViewModel.submitQuery(it) }
    ItemList(queryViewState.value.items.orEmpty())
}

@Composable
fun ItemList(items: List<Item>) {
    LazyColumnFor(items = items) {
        ContentItem(item = it)
    }
}
