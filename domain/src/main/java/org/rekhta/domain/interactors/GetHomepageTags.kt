package org.rekhta.domain.interactors

import com.kafka.data.db.*
import com.kafka.data.model.*
import javax.inject.Inject

class GetHomepageTags @Inject constructor() {
    operator fun invoke(): List<HomepageTag> {
        return listOf(
            HomepageTag("Urdu Poetry", urduPoetry.toQuery(), false),
            HomepageTag("English Prose", englishProse.toQuery(), false),
            HomepageTag("Devnagri", devnagri.toQuery(), false),
            HomepageTag("English Poetry", englishPoetry.toQuery(), false),
            HomepageTag("Urdu Prose", urduProse.toQuery(), false),
            HomepageTag(
                "Suggested",
                listOf(urduPoetry, englishProse, englishPoetry, urduProse, devnagri).joinToString()
                    .toQuery(),
                true
            )
        )
    }

    private fun String.toQuery() = SearchQuery(type = SearchQueryType.Suggested(this))
}

data class SearchQuery(val text: String = "", val type: SearchQueryType)

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

data class HomepageTag(
    val title: String, val searchQuery: SearchQuery, var isSelected: Boolean = false
)
