package com.kafka.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.model.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.kafka.analytics.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.snackbar.toUiMessage
import org.kafka.domain.combine
import org.kafka.domain.interactors.AddRecentSearch
import org.kafka.domain.interactors.RemoveRecentSearch
import org.kafka.domain.interactors.SearchQueryItems
import org.kafka.domain.observers.ObserveRecentSearch
import org.kafka.navigation.Navigator
import org.kafka.navigation.Screen
import javax.inject.Inject

const val MaxRecentSearches = 30

@HiltViewModel
class SearchViewModel @Inject constructor(
    observeRecentSearch: ObserveRecentSearch,
    private val addRecentSearch: AddRecentSearch,
    private val removeRecentSearch: RemoveRecentSearch,
    private val searchQueryItems: SearchQueryItems,
    private val navigator: Navigator,
    private val analytics: Analytics,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private var keyword = savedStateHandle.getStateFlow("keyword", "")
    private val selectedFilters = MutableStateFlow(SearchFilter.all())

    val state: StateFlow<SearchViewState> = combine(
        keyword,
        selectedFilters,
        searchQueryItems.flow.onStart { emit(emptyList()) },
        observeRecentSearch.flow,
        loadingState.observable,
        uiMessageManager.message,
    ) { keyword, filters, items, recentSearches, isLoading, message ->
        SearchViewState(
            keyword = keyword,
            filters = filters,
            items = items,
            recentSearches = recentSearches.take(MaxRecentSearches),
            isLoading = isLoading,
            message = message
        )
    }.stateInDefault(scope = viewModelScope, initialValue = SearchViewState())

    init {
        savedStateHandle.get<String>("filters")
            ?.let { selectedFilters.value = SearchFilter.from(it) }

        if (keyword.value.isNotEmpty()) {
            search(keyword.value, selectedFilters.value)
        }

        observeRecentSearch(Unit)
    }

    fun search(keyword: String, searchFilters: List<SearchFilter>) {
        analytics.log { searchQuery(keyword, searchFilters.map { it.name }) }

        viewModelScope.launch {
            loadingState.addLoader()

            searchQueryItems(SearchQueryItems.Params(keyword, searchFilters))
                .catch { throwable -> uiMessageManager.emitMessage(throwable.toUiMessage()) }
                .collect { loadingState.removeLoader() }
        }
    }

    fun addRecentSearch(keyword: String, selectedFilters: List<SearchFilter>) {
        viewModelScope.launch {
            analytics.log { this.addRecentSearch(keyword, selectedFilters.map { it.name }) }
            addRecentSearch.invoke(keyword).collect()
        }
    }

    fun toggleFilter(filter: SearchFilter) {
        val filters = state.value.filters.toMutableList()
        if (filters.contains(filter)) {
            filters.remove(filter)
        } else {
            filters.add(filter)
        }
        selectedFilters.value = filters
    }

    fun removeRecentSearch(keyword: String) {
        viewModelScope.launch {
            analytics.log { removeRecentSearch(keyword) }
            removeRecentSearch.invoke(keyword).collect()
        }
    }

    fun openItemDetail(itemId: String) {
        analytics.log { this.openItemDetail(itemId, "search") }
        navigator.navigate(Screen.ItemDetail.createRoute(navigator.currentRoot.value, itemId))
    }
}
