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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kafka.common.adaptive.fullSpanItems
import com.kafka.common.adaptive.useWideLayout
import com.kafka.common.adaptive.windowWidthSizeClass
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.extensions.rememberMutableState
import com.kafka.data.entities.Item
import com.kafka.data.model.MediaType
import com.kafka.search.widget.MediaTypeChips
import com.kafka.search.widget.SearchWidget
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.bottomScaffoldPadding
import com.kafka.ui.components.item.Item
import com.kafka.ui.components.progress.InfiniteProgressBarSmall
import com.kafka.ui.components.topScaffoldPadding
import me.tatarka.inject.annotations.Inject
import ui.common.theme.theme.Dimens

@Composable
@Inject
fun SearchScreen(viewModelFactory: (SavedStateHandle) -> SearchViewModel) {
    val searchViewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
    val searchViewState by searchViewModel.state.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Search(
                searchText = searchViewState.keyword,
                setSearchText = { searchViewModel.setQuery(it) },
                searchViewState = searchViewState,
                selectedMediaTypes = searchViewModel.selectedMediaTypes,
                onMediaTypeClicked = { searchViewModel.toggleMediaType(it) },
                onSearchClicked = { query, mediaTypes ->
                    searchViewModel.setQuery(query)
                    searchViewModel.search(query, mediaTypes)
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
    selectedMediaTypes: List<MediaType>,
    onMediaTypeClicked: (MediaType) -> Unit,
    onSearchClicked: (String, List<MediaType>) -> Unit,
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
                onSearchClicked(search.searchTerm, selectedMediaTypes)
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
            onImeAction = { query -> onSearchClicked(query, selectedMediaTypes) },
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
