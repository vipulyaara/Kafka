package com.kafka.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.model.SearchFilter
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.logging.LogCompositions
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.bottomScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.topScaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun SearchScreen() {
    LogCompositions(tag = "Search")

    val searchViewModel: SearchViewModel = hiltViewModel()
    val searchViewState by searchViewModel.state.collectAsStateWithLifecycle()
    var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = searchViewState.keyword, selection = TextRange(0)))
    }
    val onSearchClicked: (String) -> Unit = {
        if (it.isNotEmpty()) {
            searchViewModel.search(it, searchViewState.filters)
            searchViewModel.addRecentSearch(it, searchViewState.filters)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Search(
                searchText = searchText,
                setSearchText = { searchText = it },
                searchViewState = searchViewState,
                selectedFilters = searchViewState.filters,
                onFilterClicked = { searchViewModel.toggleFilter(it) },
                onSearchClicked = {
                    searchText = TextFieldValue(text = it, selection = TextRange(it.length))
                    onSearchClicked(it)
                },
                removeRecentSearch = { searchViewModel.removeRecentSearch(it) },
                openItemDetail = { searchViewModel.openItemDetail(it) }
            )
        }
    }
}

@Composable
private fun Search(
    searchText: TextFieldValue,
    setSearchText: (TextFieldValue) -> Unit,
    searchViewState: SearchViewState,
    selectedFilters: List<SearchFilter>,
    onFilterClicked: (SearchFilter) -> Unit,
    onSearchClicked: (String) -> Unit,
    removeRecentSearch: (String) -> Unit,
    openItemDetail: (String) -> Unit
) {
    val density = LocalDensity.current
    var listTopPadding by rememberMutableState { 0.dp }
    val paddingValues = PaddingValues(top = listTopPadding, bottom = bottomScaffoldPadding())

    AnimatedVisibilityFade(visible = searchViewState.items != null) {
        LazyColumn(contentPadding = paddingValues) {
            items(searchViewState.items!!) { item ->
                Item(
                    item = item,
                    modifier = Modifier.padding(Dimens.Gutter, Dimens.Spacing06)
                ) { itemId -> openItemDetail(itemId) }
            }
        }
    }

    if (searchViewState.canShowRecentSearches) {
        RecentSearches(
            recentSearches = searchViewState.recentSearches!!.map { it.searchTerm },
            onSearchClicked = onSearchClicked,
            onRemoveSearch = removeRecentSearch,
            contentPadding = paddingValues
        )
    }

    AnimatedVisibilityFade(visible = searchViewState.isLoading) {
        InfiniteProgressBar(modifier = Modifier.padding(paddingValues))
    }

    Column(modifier = Modifier.onGloballyPositioned {
        listTopPadding = with(density) { it.size.height.toDp() }
    }) {
        SearchWidget(
            searchText = searchText,
            setSearchText = setSearchText,
            onImeAction = onSearchClicked,
            modifier = Modifier.padding(top = topScaffoldPadding())
        )

        SearchFilterChips(selectedFilters = selectedFilters, onFilterClicked = onFilterClicked)
    }
}
