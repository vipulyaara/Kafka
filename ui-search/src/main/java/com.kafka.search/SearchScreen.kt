package com.kafka.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kafka.data.entities.Item
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import org.kafka.common.LogCompositions
import org.kafka.common.extensions.rememberStateWithLifecycle
import org.kafka.common.widgets.DefaultScaffold
import org.kafka.item.ArchiveQueryViewModel
import org.kafka.item.Item
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.ui.components.progress.InfiniteProgressBar
import ui.common.theme.theme.textSecondary

@Composable
fun SearchScreen() {
    LogCompositions(tag = "Search")

    val navigator = LocalNavigator.current
    val searchViewModel: ArchiveQueryViewModel = hiltViewModel()
    val searchViewState by rememberStateWithLifecycle(searchViewModel.state)

    var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(text = "", selection = TextRange(0))
        )
    }

    DefaultScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { Spacer(modifier = Modifier.height(56.dp)) }
    ) {
        Column {
            SearchWidget(
                searchText = searchText,
                setSearchText = { searchText = it },
                onImeAction = {
                    searchViewModel.submitQuery(ArchiveQuery().apply { booksByAuthor(it) })
                }
            )

            searchViewState.items?.let {
                SearchResults(results = it) {
                    navigator.navigate(LeafScreen.ItemDetail.createRoute(it))
                }
            }
            InfiniteProgressBar(show = searchViewState.isLoading)
        }
    }
}

@Composable
private fun SearchResults(results: List<Item>, openContentDetail: (String) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(vertical = 24.dp)) {
        items(results) {
            Item(item = it, openItemDetail = openContentDetail)
        }
    }
}

@Composable
fun SearchResultLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(top = 24.dp, start = 12.dp, end = 24.dp, bottom = 12.dp),
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.textSecondary
    )
}
