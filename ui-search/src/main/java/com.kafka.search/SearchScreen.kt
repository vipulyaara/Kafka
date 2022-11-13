package com.kafka.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Item
import org.kafka.common.LogCompositions
import org.kafka.item.ArchiveQueryViewModel
import org.kafka.item.Item
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.ui.components.progress.InfiniteProgressBar
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textSecondary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen() {
    LogCompositions(tag = "Search")

    val navigator = LocalNavigator.current
    val queryViewModel: ArchiveQueryViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val queryViewState by queryViewModel.state.collectAsStateWithLifecycle()

    val recentSearches by searchViewModel.recentSearches.collectAsStateWithLifecycle()

    var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(text = "", selection = TextRange(0))
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { Spacer(modifier = Modifier.height(56.dp)) }
    ) {
        Column {
            Box(modifier = Modifier.height(24.dp))
            SearchWidget(
                searchText = searchText,
                setSearchText = { searchText = it },
                onImeAction = {
                    queryViewModel.submitQuery(it)
                    searchViewModel.addRecentSearch(it)
                }
            )

            queryViewState.items?.let {
                SearchResults(results = it) {
                    navigator.navigate(LeafScreen.ItemDetail.createRoute(it))
                }
            }

            if (recentSearches.isNotEmpty() && queryViewState.items.isNullOrEmpty() && !queryViewState.isLoading) {
                RecentSearches(
                    recentSearches = recentSearches.map { it.searchTerm },
                    onSearchClicked = {
                        queryViewModel.submitQuery(it)
                        searchViewModel.addRecentSearch(it)
                    },
                    onRemoveSearch = { searchViewModel.removeRecentSearch(it) }
                )
            }

            InfiniteProgressBar(show = queryViewState.isLoading)
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
        modifier = modifier.padding(start = Dimens.Spacing12, end = 24.dp, bottom = Dimens.Spacing12),
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.textSecondary
    )
}
