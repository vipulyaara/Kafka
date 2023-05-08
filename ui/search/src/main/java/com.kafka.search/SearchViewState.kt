package com.kafka.search

import com.kafka.data.entities.Item
import com.kafka.data.entities.RecentSearch
import com.kafka.data.model.SearchFilter
import org.kafka.common.snackbar.UiMessage

data class SearchViewState(
    val filters: List<SearchFilter> = SearchFilter.values().toList(),
    var items: List<Item>? = null,
    val recentSearches: List<String>? = null,
    val isLoading: Boolean = false,
    val message: UiMessage? = null
) {
    val canShowRecentSearches: Boolean
        get() = items.isNullOrEmpty()
                && !recentSearches.isNullOrEmpty() && !isLoading
}
