package org.kafka.domain.interactors

import com.kafka.data.db.devnagri
import com.kafka.data.db.englishPoetry
import com.kafka.data.db.englishProse
import com.kafka.data.db.kafkaArchives
import com.kafka.data.db.urduPoetry
import com.kafka.data.db.urduProse
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import com.kafka.data.model.booksByCollection
import com.kafka.data.model.booksByIdentifiers
import com.kafka.data.model.booksByTitleKeyword
import com.kafka.data.model.booksByTitleOrCreator
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
                listOf(
                    kafkaArchives,
                    englishPoetry.split(" ,").sublistSuggested(),
                    urduPoetry.split(" ,").sublistSuggested(),
                    devnagri.split(" ,").sublistSuggested(),
                    englishProse.split(" ,").sublistSuggested(),
//                    urduProse.split(" ,").sublistSuggested()
                ).joinToString().toQuery(),
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
    object Search : SearchQueryType()
    data class Suggested(val identifiers: String) : SearchQueryType()
}

fun SearchQuery.asArchiveQuery() = ArchiveQuery().apply {
    when (type) {
        SearchQueryType.Creator -> booksByAuthor(text)
        SearchQueryType.Title -> booksByTitleKeyword(text)
        SearchQueryType.Collection -> booksByCollection(text)
        SearchQueryType.Search -> booksByTitleOrCreator(text)
        is SearchQueryType.Suggested -> booksByIdentifiers(type.identifiers)
    }
}

data class HomepageTag(
    val title: String, val searchQuery: SearchQuery, var isSelected: Boolean = false
)

private const val suggestedItemsSize = 20
private fun <E> List<E>.sublistSuggested() =
    subList(0, size.coerceAtMost(suggestedItemsSize)).joinToString()
