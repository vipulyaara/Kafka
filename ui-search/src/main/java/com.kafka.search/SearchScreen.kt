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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.RecentSearch
import org.kafka.common.extensions.AnimatedVisibilityFade
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

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Search(
                searchText = searchText,
                setSearchText = { searchText = it },
                queryViewModel = queryViewModel,
                searchViewModel = searchViewModel,
                queryViewState = queryViewState,
                recentSearches = recentSearches,
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
    recentSearches: List<RecentSearch>,
) {
    val navigator = LocalNavigator.current
    val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()
    val scaffoldPadding = scaffoldPadding()
    val selectedFilters = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { mutableStateListOf(*it.toTypedArray()) }
        )
    ) { mutableStateListOf(*SearchFilter.values()) }

    LaunchedEffect(Unit) {
        if (searchText.text.isNotEmpty()) {
            queryViewModel.submitQuery(searchText.text, selectedFilters)
        }
    }

    val onSearchClicked: (String) -> Unit = {
        queryViewModel.submitQuery(it, selectedFilters)
        searchViewModel.addRecentSearch(it, selectedFilters)
    }

    Column(modifier = Modifier.padding(top = scaffoldPadding.calculateTopPadding())) {
        SearchWidget(
            searchText = searchText,
            setSearchText = setSearchText,
            onImeAction = onSearchClicked
        )

        SearchFilterChips(selectedFilters)

        AnimatedVisibilityFade(visible = queryViewState.items != null) {
            LazyColumn(contentPadding = PaddingValues(bottom = bottomScaffoldPadding())) {
                items(queryViewState.items!!) {
                    Item(item = it) { itemId ->
                        navigator.navigate(Screen.ItemDetail.createRoute(currentRoot, itemId))
                    }
                }
            }
        }

        val isResultEmpty = queryViewState.items.isNullOrEmpty() && !queryViewState.isLoading

        if (isResultEmpty && recentSearches.isNotEmpty()) {
            RecentSearches(
                recentSearches = recentSearches,
                searchViewModel = searchViewModel,
                setSearchText = setSearchText,
                onSearchClicked = onSearchClicked
            )
        }

        InfiniteProgressBar(show = queryViewState.isLoading)
    }
}

@Composable
private fun RecentSearches(
    recentSearches: List<RecentSearch>,
    searchViewModel: SearchViewModel,
    setSearchText: (TextFieldValue) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    RecentSearches(recentSearches = recentSearches.map { it.searchTerm }, onSearchClicked = {
        setSearchText(TextFieldValue(text = it, selection = TextRange(it.lastIndex)))
        onSearchClicked(it)
    }, onRemoveSearch = { searchViewModel.removeRecentSearch(it) })
}
