package com.kafka.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.model.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kafka.analytics.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.domain.interactors.AddRecentSearch
import org.kafka.domain.interactors.RemoveRecentSearch
import org.kafka.domain.interactors.SearchQueryItems
import org.kafka.domain.observers.ObserveRecentSearch
import org.kafka.domain.observers.ObserveSearchItems
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
    private val observeSearchItems: ObserveSearchItems,
    private val navigator: Navigator,
    private val analytics: Analytics,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    var keyword by mutableStateOf("")

    private val selectedFilters = MutableStateFlow(SearchFilter.all())

    val state: StateFlow<SearchViewState> = kotlinx.coroutines.flow.combine(
        selectedFilters,
        observeSearchItems.flow,
        observeRecentSearch.flow.map {
            it.take(MaxRecentSearches).distinctBy { it.id }.map { it.searchTerm }
        },
        loadingState.observable,
        uiMessageManager.message,
    ) { filters, items, recentSearches, isLoading, message ->
        SearchViewState(
            filters = filters,
            items = items,
            recentSearches = recentSearches,
            isLoading = isLoading,
            message = message
        )
    }.stateInDefault(scope = viewModelScope, initialValue = SearchViewState())

    init {
        savedStateHandle.get<String>("filters")
            ?.let { selectedFilters.value = SearchFilter.from(it) }

        savedStateHandle.get<String>("keyword")?.takeIf { it.isNotEmpty() }
            ?.let { keyword = it }

        search(keyword, selectedFilters.value)

        observeRecentSearch(Unit)
    }

    fun search(keyword: String, filters: List<SearchFilter>) {
        if (keyword.isEmpty()) return

        analytics.log { searchQuery(keyword, filters.map { it.name }) }

        addRecentSearch(keyword, filters)

        observeSearchItems(ObserveSearchItems.Params(keyword, filters))

        viewModelScope.launch {
            searchQueryItems(SearchQueryItems.Params(keyword, filters))
                .collectStatus(loadingState, uiMessageManager)
        }
    }

    private fun addRecentSearch(keyword: String, selectedFilters: List<SearchFilter>) {
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
