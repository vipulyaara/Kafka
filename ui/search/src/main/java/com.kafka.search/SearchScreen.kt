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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.RecentSearch
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.logging.LogCompositions
import org.kafka.item.ArchiveQueryViewModel
import org.kafka.item.ArchiveQueryViewState
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Screen
import org.kafka.navigation.SearchFilter
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.bottomScaffoldPadding
import org.kafka.ui.components.item.Item
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.topScaffoldPadding
import ui.common.theme.theme.Dimens

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
    val selectedFilters by searchViewModel.selectedFilters.collectAsStateWithLifecycle()
    val onSearchClicked: (String) -> Unit = {
        if (it.isNotEmpty()) {
            queryViewModel.submitQuery(it, selectedFilters)
            searchViewModel.addRecentSearch(it, selectedFilters)
        }
    }

    val navigator = LocalNavigator.current
    val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { onSearchClicked(searchText.text) }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Search(
                searchText = searchText,
                setSearchText = { searchText = it },
                queryViewState = queryViewState,
                recentSearches = recentSearches,
                selectedFilters = selectedFilters,
                onSearchClicked = {
                    searchText = TextFieldValue(text = it, selection = TextRange(it.length))
                    onSearchClicked(it)
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
    val density = LocalDensity.current
    var listTopPadding by rememberMutableState { 0.dp }
    val paddingValues = PaddingValues(top = listTopPadding, bottom = bottomScaffoldPadding())

    AnimatedVisibilityFade(visible = queryViewState.items != null) {
        LazyColumn(contentPadding = paddingValues) {
            items(queryViewState.items!!) { item ->
                Item(
                    item = item,
                    modifier = Modifier.padding(Dimens.Gutter, Dimens.Spacing06)
                ) { itemId -> openItemDetail(itemId) }
            }
        }
    }

    if (queryViewState.canShowRecentSearches && recentSearches.isNotEmpty()) {
        RecentSearches(
            recentSearches = recentSearches.map { it.searchTerm },
            onSearchClicked = onSearchClicked,
            onRemoveSearch = removeRecentSearch,
            contentPadding = paddingValues
        )
    }

    AnimatedVisibilityFade(visible = queryViewState.isLoading) {
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

        SearchFilterChips(selectedFilters = selectedFilters)
    }
}
