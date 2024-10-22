package com.kafka.search

import com.kafka.data.entities.Item
import com.kafka.data.entities.RecentSearch

data class SearchViewState(
    val keyword: String = "",
    val items: List<Item>? = null,
    val recentSearches: List<RecentSearch>? = null,
    val isLoading: Boolean = false
) {
    val canShowRecentSearches: Boolean
        get() = items.isNullOrEmpty()
                && !recentSearches.isNullOrEmpty() && !isLoading
}
