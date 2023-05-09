package com.kafka.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Search(
                searchText = searchViewModel.keyword,
                setSearchText = { searchViewModel.keyword = it },
                searchViewState = searchViewState,
                selectedFilters = searchViewState.filters,
                onFilterClicked = { searchViewModel.toggleFilter(it) },
                onSearchClicked = {
                    searchViewModel.keyword = it
                    searchViewModel.search(it, searchViewState.filters)
                },
                removeRecentSearch = { searchViewModel.removeRecentSearch(it) },
                openItemDetail = { searchViewModel.openItemDetail(it) }
            )
        }
    }
}

@Composable
private fun Search(
    searchText: String,
    setSearchText: (String) -> Unit,
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

    if (searchText.isEmpty() && searchViewState.canShowRecentSearches) {
        RecentSearches(
            recentSearches = searchViewState.recentSearches!!,
            onSearchClicked = onSearchClicked,
            onRemoveSearch = removeRecentSearch,
            contentPadding = paddingValues
        )
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Spacing12, vertical = Dimens.Spacing08)
        ) {
            SearchFilterChips(selectedFilters = selectedFilters, onFilterClicked = onFilterClicked)
            InfiniteProgressBar(
                show = searchViewState.isLoading,
                modifier = Modifier.height(Dimens.Spacing24)
            )
        }
    }
}
