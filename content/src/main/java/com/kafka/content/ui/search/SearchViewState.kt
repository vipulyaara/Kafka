package com.kafka.content.ui.search

import com.data.base.model.ArchiveQuery
import com.data.base.model.booksByAuthor
import com.data.base.model.booksByCollection
import com.data.base.model.booksByKeyword
import com.kafka.data.entities.Item
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class SearchViewState(
    var query: String? = null,
    var items: List<Item>? = null,
    var isLoading: Boolean = true,
    val error: Throwable? = null
) : BaseViewState

data class SearchQuery(val text: String? = null, val type: SearchQueryType = SearchQueryType.Creator)

sealed class SearchQueryType {
    object Creator : SearchQueryType()
    object Title : SearchQueryType()
    object Collection : SearchQueryType()
}

fun SearchQuery.asArchiveQuery() = ArchiveQuery().apply {
    when (type) {
        SearchQueryType.Creator -> booksByAuthor(text)
        SearchQueryType.Title -> booksByKeyword(text)
        SearchQueryType.Collection -> booksByCollection(text)
    }
}

