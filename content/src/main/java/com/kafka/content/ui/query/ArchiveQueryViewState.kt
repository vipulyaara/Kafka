package com.kafka.content.ui.query

import com.data.base.model.*
import com.kafka.data.entities.Item
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ArchiveQueryViewState(
    var items: List<Item>? = null,
    val recentSearches: List<String>? = null,
    var isLoading: Boolean = true,
    val error: Throwable? = null
) : BaseViewState

data class SearchQuery(val text: String = "", val type: SearchQueryType = SearchQueryType.Creator)

sealed class SearchQueryType {
    object Creator : SearchQueryType()
    object Title : SearchQueryType()
    object Collection : SearchQueryType()
    object TitleOrCreator : SearchQueryType()
    data class Suggested(val identifiers: String) : SearchQueryType()
}

fun SearchQuery.asArchiveQuery() = ArchiveQuery().apply {
    when (type) {
        SearchQueryType.Creator -> booksByAuthor(text)
        SearchQueryType.Title -> booksByTitleKeyword(text)
        SearchQueryType.Collection -> booksByCollection(text)
        SearchQueryType.TitleOrCreator -> booksByTitleOrCreator(text)
        is SearchQueryType.Suggested -> booksByIdentifiers(type.identifiers)
    }
}

