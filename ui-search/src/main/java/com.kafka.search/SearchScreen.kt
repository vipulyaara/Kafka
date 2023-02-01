package com.kafka.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.kafka.data.entities.RecentSearch
import org.kafka.common.LogCompositions
import org.kafka.item.ArchiveQueryViewModel
import org.kafka.item.ArchiveQueryViewState
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.navigation.RootScreen
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textSecondary

@Composable
fun SearchScreen() {
    LogCompositions(tag = "Search")

    val navigator = LocalNavigator.current
    val queryViewModel: ArchiveQueryViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val queryViewState by queryViewModel.state.collectAsStateWithLifecycle()

    val recentSearches by searchViewModel.recentSearches.collectAsStateWithLifecycle()

    var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = "", selection = TextRange(0)))
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Search(
                searchText = searchText,
                setSearchText = { searchText = it },
                queryViewModel = queryViewModel,
                searchViewModel = searchViewModel,
                queryViewState = queryViewState,
                navigator = navigator,
                recentSearches = recentSearches
            )
        }
    }
}

@Composable
private fun Search(
    searchText: TextFieldValue,
    setSearchText: (TextFieldValue) -> Unit,
    queryViewModel: ArchiveQueryViewModel,
    searchViewModel: SearchViewModel,
    queryViewState: ArchiveQueryViewState,
    navigator: Navigator,
    recentSearches: List<RecentSearch>
) {
    Column {
        SearchWidget(searchText = searchText, setSearchText = setSearchText, onImeAction = {
            queryViewModel.submitQuery(it)
            searchViewModel.addRecentSearch(it)
        })

        AnimatedVisibility(visible = queryViewState.items != null) {
            val padding = PaddingValues(bottom = scaffoldPadding().calculateBottomPadding())
            LazyColumn(contentPadding = padding) {
                items(queryViewState.items!!) {
                    Item(item = it) { itemId ->
                        navigator.navigate(
                            LeafScreen.ItemDetail.buildRoute(
                                itemId,
                                RootScreen.Search
                            )
                        )
                    }
                }
            }
        }

        if (queryViewState.items == null) {
            RecentSearches(
                recentSearches = recentSearches,
                queryViewState = queryViewState,
                queryViewModel = queryViewModel,
                searchViewModel = searchViewModel,
                setSearchText = setSearchText
            )
        }

        InfiniteProgressBar(show = queryViewState.isLoading)
    }
}

@Composable
private fun RecentSearches(
    recentSearches: List<RecentSearch>,
    queryViewState: ArchiveQueryViewState,
    queryViewModel: ArchiveQueryViewModel,
    searchViewModel: SearchViewModel,
    setSearchText: (TextFieldValue) -> Unit
) {
    if (recentSearches.isNotEmpty() && queryViewState.items.isNullOrEmpty() && !queryViewState.isLoading) {
        RecentSearches(recentSearches = recentSearches.map { it.searchTerm }, onSearchClicked = {
            setSearchText(TextFieldValue(text = it, selection = TextRange(it.lastIndex)))
            queryViewModel.submitQuery(it)
            searchViewModel.addRecentSearch(it)
        }, onRemoveSearch = { searchViewModel.removeRecentSearch(it) })
    }
}

@Composable
fun SearchResultLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(
            start = Dimens.Spacing12, end = 24.dp, bottom = Dimens.Spacing12
        ),
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.textSecondary
    )
}
