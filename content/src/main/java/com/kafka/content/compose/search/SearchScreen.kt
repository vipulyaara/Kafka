package com.kafka.content.compose.search

import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.viewModel
import com.kafka.content.compose.Actions
import com.kafka.content.compose.item.ContentItem
import com.kafka.content.ui.query.ArchiveQueryViewModel
import com.kafka.content.ui.query.SearchQuery
import com.kafka.content.ui.search.SearchViewModel
import com.kafka.data.entities.Item

@ExperimentalLazyDsl
@Composable
fun Search(actions: Actions) {
    val searchViewModel: SearchViewModel = viewModel()
    val archiveQueryViewModel: ArchiveQueryViewModel = viewModel()
    val searchViewState by searchViewModel.state.collectAsState()
    val queryViewState by archiveQueryViewModel.state.collectAsState()

    remember { archiveQueryViewModel.submitQuery(SearchQuery("Einstein")) }

    queryViewState.items?.let { SearchResults(it, actions) }
}

@ExperimentalLazyDsl
@Composable
fun SearchResults(items: List<Item>, actions: Actions) {
    val itemDetailAction: (Item) -> Unit = { actions.itemDetail(it.itemId) }
    LazyColumn(content = {
        items(items = items) {
            ContentItem(item = it, onItemClick = itemDetailAction)
        }
    })
}
