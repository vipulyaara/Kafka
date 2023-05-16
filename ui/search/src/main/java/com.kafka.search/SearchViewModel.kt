package com.kafka.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.model.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.kafka.analytics.Analytics
import org.kafka.base.debug
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

    var keyword by mutableStateOf(savedStateHandle.get<String>("keyword").orEmpty())
    var filters by mutableStateOf(
        savedStateHandle.get<String>("filters")?.let { SearchFilter.from(it) }
            ?: SearchFilter.all())

    val state: StateFlow<SearchViewState> = combine(
        observeSearchItems.flow.onStart {
            if (keyword.isEmpty()) {
                debug { "keyword is empty" }
                emit(listOf())
            }
        },
        observeRecentSearch.flow,
        loadingState.observable,
        uiMessageManager.message,
        ::SearchViewState,
    ).stateInDefault(scope = viewModelScope, initialValue = SearchViewState())

    init {
        search(keyword, filters)
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
        val filters = filters.toMutableList()
        if (filters.contains(filter)) {
            filters.remove(filter)
        } else {
            filters.add(filter)
        }
        this.filters = filters
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
