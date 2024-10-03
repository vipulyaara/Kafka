package com.kafka.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.Item
import com.kafka.data.model.MediaType
import com.kafka.data.model.SearchFilter
import org.kafka.common.adaptive.fullSpanItems
import org.kafka.common.adaptive.useWideLayout
import org.kafka.common.adaptive.windowWidthSizeClass
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.logging.LogCompositions
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.bottomScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.progress.InfiniteProgressBarSmall
import org.kafka.ui.components.topScaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun SearchScreen(searchViewModel: SearchViewModel) {
    LogCompositions(tag = "Search")

    val searchViewState by searchViewModel.state.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Search(
                searchText = searchViewState.keyword,
                setSearchText = { searchViewModel.setQuery(it) },
                searchViewState = searchViewState,
                selectedFilters = searchViewState.selectedFilters,
                onFilterClicked = { searchViewModel.toggleFilter(it) },
                selectedMediaTypes = searchViewModel.selectedMediaTypes,
                onMediaTypeClicked = { searchViewModel.toggleMediaType(it) },
                onSearchClicked = { query, filters, mediaTypes ->
                    searchViewModel.setQuery(query)
                    searchViewModel.search(query, filters, mediaTypes)
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
    selectedMediaTypes: List<MediaType>,
    onMediaTypeClicked: (MediaType) -> Unit,
    onSearchClicked: (String, List<SearchFilter>, List<MediaType>) -> Unit,
    removeRecentSearch: (String) -> Unit,
    openItemDetail: (String) -> Unit,
) {
    val useWideLayout = windowWidthSizeClass().useWideLayout()
    val density = LocalDensity.current
    var listTopPadding by rememberMutableState { 0.dp }
    val paddingValues = PaddingValues(top = listTopPadding, bottom = bottomScaffoldPadding())

    if (!searchViewState.items.isNullOrEmpty()) {
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = paddingValues) {
            if (useWideLayout) {
                items(searchViewState.items) { item ->
                    SearchResultItem(item, openItemDetail)
                }
            } else {
                fullSpanItems(searchViewState.items) { item ->
                    SearchResultItem(item, openItemDetail)
                }
            }
        }
    }

    AnimatedVisibilityFade(searchText.isEmpty() && searchViewState.canShowRecentSearches) {
        RecentSearches(
            recentSearches = searchViewState.recentSearches!!,
            onSearchClicked = { search ->
                onSearchClicked(search.searchTerm, selectedFilters, selectedMediaTypes)
            },
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
            onImeAction = { query -> onSearchClicked(query, selectedFilters, selectedMediaTypes) },
            modifier = Modifier.padding(top = topScaffoldPadding())
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Spacing12, vertical = Dimens.Spacing08)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.Spacing08)) {
                SearchFilterChips(
                    selectedFilters = selectedFilters,
                    onFilterClicked = onFilterClicked
                )
                MediaTypeChips(
                    selectedMediaTypes = selectedMediaTypes,
                    onFilterClicked = onMediaTypeClicked
                )
            }
            InfiniteProgressBarSmall(
                show = searchViewState.isLoading,
                modifier = Modifier.height(Dimens.Spacing24)
            )
        }
    }
}

@Composable
private fun SearchResultItem(item: Item, openItemDetail: (String) -> Unit) {
    Item(
        item = item,
        modifier = Modifier
            .clickable { openItemDetail(item.itemId) }
            .padding(Dimens.Gutter, Dimens.Spacing06)
    )
}
