package com.kafka.ui.search

import com.kafka.data.entities.Item
import com.kafka.data.model.RowItems
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class SearchViewState(
    var query: String? = null,
    var favorites: List<Item>? = null,
    var homepageItems: RowItems = RowItems(),
    var isLoading: Boolean = false,
    val error: Throwable? = null
) : BaseViewState

data class SearchQuery(val text: String? = null, val type: SearchQueryType)

sealed class SearchQueryType {
    object Creator : SearchQueryType()
    object Title : SearchQueryType()
    object Collection : SearchQueryType()
}

