package com.kafka.search

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.ObservableLoadingCounter
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.toUiMessage
import com.kafka.data.entities.Item
import com.kafka.data.model.MediaType
import com.kafka.domain.interactors.AddRecentSearch
import com.kafka.domain.interactors.RemoveRecentSearch
import com.kafka.domain.interactors.SearchQueryItems
import com.kafka.domain.observers.ObserveRecentSearch
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    observeRecentSearch: ObserveRecentSearch,
    private val addRecentSearch: AddRecentSearch,
    private val removeRecentSearch: RemoveRecentSearch,
    private val searchQueryItems: SearchQueryItems,
    private val navigator: Navigator,
    private val analytics: Analytics,
    private val snackbarManager: SnackbarManager,
    private val loadingState: ObservableLoadingCounter,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val route = savedStateHandle.run {
        Screen.Search(keyword = savedStateHandle.get<String>(extraKeyword).orEmpty())
    }
    internal val selectedMediaTypes = mutableStateListOf<MediaType>()
        .apply { addAll(MediaType.entries) }
    private val searchResults = MutableStateFlow<List<Item>>(listOf())

    val state: StateFlow<SearchViewState> = combine(
        savedStateHandle.getStateFlow(extraKeyword, ""),
        searchResults,
        observeRecentSearch.flow,
        loadingState.observable
    ) { keyword, items, recentSearches, isLoading ->
        SearchViewState(
            keyword = keyword,
            items = items,
            recentSearches = recentSearches,
            isLoading = isLoading
        )
    }.stateInDefault(scope = viewModelScope, initialValue = SearchViewState())

    init {
        search(
            keyword = route.keyword,
            mediaTypes = selectedMediaTypes
        )

        observeRecentSearch(Unit)
    }

    fun search(
        keyword: String = state.value.keyword,
        mediaTypes: List<MediaType> = selectedMediaTypes,
    ) {
        viewModelScope.launch {
            loadingState.addLoader()

            searchResults.value = searchQueryItems(
                SearchQueryItems.Params(keyword = keyword.trim(), mediaTypes = mediaTypes.toList())
            ).also { result ->
                result.onException { snackbarManager.addMessage(it.toUiMessage()) }
            }.getOrElse { emptyList() }

            loadingState.removeLoader()
        }

        if (keyword.isNotEmpty()) {
            analytics.log {
                searchQuery(keyword = keyword, mediaTypes = mediaTypes.map { it.value })
            }
            addRecentSearch(keyword)
        }
    }

    fun setQuery(query: String) {
        savedStateHandle[extraKeyword] = query
    }

    private fun addRecentSearch(keyword: String) {
        viewModelScope.launch {
            val params = AddRecentSearch.Params(
                searchTerm = keyword,
                mediaTypes = selectedMediaTypes.toList()
            )
            addRecentSearch.invoke(params)
        }
    }

    fun toggleMediaType(mediaType: MediaType) {
        if (selectedMediaTypes.contains(mediaType)) {
            if (selectedMediaTypes.size > 1) {
                selectedMediaTypes.remove(mediaType)
            }
        } else {
            selectedMediaTypes.add(mediaType)
        }

        if (state.value.keyword.isNotEmpty()) {
            search(mediaTypes = selectedMediaTypes)
        }
    }

    fun removeRecentSearch(keyword: String) {
        viewModelScope.launch {
            analytics.log { removeRecentSearch(keyword) }
            removeRecentSearch.invoke(keyword)
        }
    }

    fun openItemDetail(itemId: String) {
        analytics.log { this.openItemDetail(itemId, "search") }
        navigator.navigate(Screen.ItemDetail(itemId))
    }
}

private const val extraKeyword = "keyword"
