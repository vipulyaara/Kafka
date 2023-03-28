package com.kafka.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.RecentSearch
import org.kafka.common.logging.LogCompositions
import org.kafka.item.ArchiveQueryViewModel
import org.kafka.item.ArchiveQueryViewState
import org.kafka.item.SearchFilter
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Screen
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.bottomScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding

@Composable
fun SearchScreen() {
    LogCompositions(tag = "Search")

    val queryViewModel: ArchiveQueryViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val queryViewState by queryViewModel.state.collectAsStateWithLifecycle()
    val recentSearches by searchViewModel.recentSearches.collectAsStateWithLifecycle()
    val keywordState by searchViewModel.keyword.collectAsStateWithLifecycle()
    var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = keywordState, selection = TextRange(0)))
    }
    val selectedFilters = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { mutableStateListOf(*it.toTypedArray()) }
        )
    ) { mutableStateListOf(*SearchFilter.values()) }

    val navigator = LocalNavigator.current
    val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (searchText.text.isNotEmpty()) {
            queryViewModel.submitQuery(searchText.text, selectedFilters)
            searchViewModel.addRecentSearch(searchText.text, selectedFilters)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Search(
                searchText = searchText,
                setSearchText = { searchText = it },
                queryViewState = queryViewState,
                recentSearches = recentSearches,
                selectedFilters = selectedFilters,
                onSearchClicked = {
                    navigator.navigate(Screen.Search.createRoute(currentRoot, it))
                },
                removeRecentSearch = { searchViewModel.removeRecentSearch(it) },
                openItemDetail = {
                    navigator.navigate(Screen.ItemDetail.createRoute(currentRoot, it))
                }
            )
        }
    }
}

@Composable
private fun Search(
    searchText: TextFieldValue,
    setSearchText: (TextFieldValue) -> Unit,
    queryViewState: ArchiveQueryViewState,
    recentSearches: List<RecentSearch>,
    selectedFilters: SnapshotStateList<SearchFilter>,
    onSearchClicked: (String) -> Unit,
    removeRecentSearch: (String) -> Unit,
    openItemDetail: (String) -> Unit
) {
    Column(modifier = Modifier.padding(top = scaffoldPadding().calculateTopPadding())) {
        SearchWidget(
            searchText = searchText,
            setSearchText = setSearchText,
            onImeAction = onSearchClicked
        )

        SearchFilterChips(selectedFilters)

        LazyColumn(contentPadding = PaddingValues(bottom = bottomScaffoldPadding())) {
            queryViewState.items?.let { items ->
                items(items) {
                    Item(item = it) { itemId -> openItemDetail(itemId) }
                }
            }

            item {
                if (queryViewState.canShowRecentSearches && recentSearches.isNotEmpty()) {
                    RecentSearches(
                        recentSearches = recentSearches.map { it.searchTerm },
                        onSearchClicked = { onSearchClicked(it) },
                        onRemoveSearch = removeRecentSearch
                    )
                }
            }

            item {
                InfiniteProgressBar(show = queryViewState.isLoading)
            }
        }
    }
}
