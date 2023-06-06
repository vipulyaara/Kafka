package com.kafka.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.model.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.kafka.analytics.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.common.getMutableStateFlow
import org.kafka.domain.combine
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

    private val keywordInitialValue = savedStateHandle.get<String>(extraKeyword).orEmpty()
    private val keywordFlow = savedStateHandle
        .getMutableStateFlow(extraKeyword, "", viewModelScope)
    private val filtersFlow = savedStateHandle
        .getMutableStateFlow(extraFilters, SearchFilter.allString(), viewModelScope)

    val state: StateFlow<SearchViewState> = combine(
        keywordFlow,
        filtersFlow.map { SearchFilter.from(it) },
        observeSearchItems.flow.onStart { if (keywordInitialValue.isEmpty()) emit(emptyList()) },
        observeRecentSearch.flow,
        loadingState.observable,
        uiMessageManager.message,
        ::SearchViewState
    ).stateInDefault(scope = viewModelScope, initialValue = SearchViewState())

    init {
        val filters = savedStateHandle.get<String>(extraFilters) ?: SearchFilter.allString()
        search(keywordInitialValue, SearchFilter.from(filters))

        observeRecentSearch(Unit)
    }

    fun setKeyword(keyword: String) {
        keywordFlow.value = keyword
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
        this.filtersFlow.value = SearchFilter.toString(filters)
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

private const val extraKeyword = "keyword"
private const val extraFilters = "filters"
