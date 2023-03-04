package com.kafka.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kafka.analytics.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.domain.interactors.AddRecentSearch
import org.kafka.domain.interactors.RemoveRecentSearch
import org.kafka.domain.observers.ObserveRecentSearch
import org.kafka.item.SearchFilter
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    observeRecentSearch: ObserveRecentSearch,
    private val addRecentSearch: AddRecentSearch,
    private val removeRecentSearch: RemoveRecentSearch,
    private val analytics: Analytics,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var keyword = savedStateHandle.getStateFlow("keyword", "")
    val recentSearches = observeRecentSearch.flow.stateInDefault(viewModelScope, emptyList())

    init {
        observeRecentSearch(Unit)
    }

    fun addRecentSearch(keyword: String, selectedFilters: List<SearchFilter>) {
        viewModelScope.launch {
            analytics.log { this.addRecentSearch(keyword, selectedFilters.map { it.name }) }
            addRecentSearch.invoke(keyword).collect()
        }
    }

    fun removeRecentSearch(keyword: String) {
        viewModelScope.launch {
            analytics.log { removeRecentSearch(keyword) }
            removeRecentSearch.invoke(keyword).collect()
        }
    }
}


