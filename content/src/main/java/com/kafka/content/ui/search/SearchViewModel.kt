package com.kafka.content.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.kafka.content.domain.search.AddRecentSearch
import com.kafka.content.domain.search.ObserveRecentSearch
import com.kafka.content.ui.query.ArchiveQueryViewState
import com.kafka.content.ui.query.SearchQuery
import com.kafka.ui_common.base.ReduxViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val observeRecentSearch: ObserveRecentSearch,
    private val addRecentSearch: AddRecentSearch
) : ReduxViewModel<ArchiveQueryViewState>(ArchiveQueryViewState()) {
    private var recentSearches = listOf<String>()

    init {
        viewModelScope.launch { observeRecentSearch.observe().collect { recentSearches = it } }
        observeRecentSearch(Unit)
    }

    fun addRecentSearch(searchQuery: SearchQuery) {
        addRecentSearch(searchQuery.text)
    }

    fun onKeywordChanged(keyword: String) {
        if (keyword.length > 2) {
            viewModelScope.launchSetState {
                copy(recentSearches = this@SearchViewModel.recentSearches
                    .filter { it.startsWith(keyword, true) }.distinct())
            }
        } else {
            viewModelScope.launchSetState { copy(recentSearches = null) }
        }
    }
}

