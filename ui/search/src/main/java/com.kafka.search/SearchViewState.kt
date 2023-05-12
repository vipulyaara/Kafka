package com.kafka.search

import com.kafka.data.entities.Item
import org.kafka.common.snackbar.UiMessage

data class SearchViewState(
    var items: List<Item>? = null,
    val recentSearches: List<String>? = null,
    val isLoading: Boolean = false,
    val message: UiMessage? = null
) {
    val canShowRecentSearches: Boolean
        get() = items.isNullOrEmpty()
                && !recentSearches.isNullOrEmpty() && !isLoading
}
