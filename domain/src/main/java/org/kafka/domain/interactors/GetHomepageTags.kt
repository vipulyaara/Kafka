package org.kafka.domain.interactors

import com.kafka.data.db.devnagri
import com.kafka.data.db.englishPoetry
import com.kafka.data.db.englishProse
import com.kafka.data.db.kafkaArchives
import com.kafka.data.db.urduPoetry
import com.kafka.data.db.urduProse
import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import com.kafka.data.model.booksByCollection
import com.kafka.data.model.booksByIdentifiers
import com.kafka.data.model.booksByTitleKeyword
import com.kafka.data.model.booksByTitleOrCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.kafka.base.debug
import javax.inject.Inject

class GetHomepageTags @Inject constructor(private val firestoreGraph: FirestoreGraph) {
    suspend operator fun invoke(): List<HomepageTag> {
        val items = listOf(
            HomepageTag("Urdu Poetry", urduPoetry.toQuery(), false),
            HomepageTag("English Prose", englishProse.toQuery(), false),
            HomepageTag("Devnagri", devnagri.toQuery(), false),
            HomepageTag("English Poetry", englishPoetry.toQuery(), false),
            HomepageTag("Urdu Prose", urduProse.toQuery(), false),
            HomepageTag(
                "Suggested",
                homepageQuery().toQuery(),
                true
            )
        )

        val ids = homepageQuery()
        debug { "Homepage tags are $ids" }

        withContext(Dispatchers.IO) {
            firestoreGraph.homepageCollection.set(mapOf("ids" to ids)).await()
        }

        return items
    }

    private fun homepageQuery() = listOf(
        kafkaArchives,
        englishPoetry.split(" ,").sublistSuggested(),
        urduPoetry.split(" ,").sublistSuggested(),
        devnagri.split(" ,").sublistSuggested(),
        englishProse.split(" ,").sublistSuggested(),
        urduProse.split(" ,").sublistSuggested()
    ).joinToString()

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

private const val suggestedItemsSize = 40

private fun <E> List<E>.sublistSuggested() =
    subList(0, size.coerceAtMost(suggestedItemsSize)).joinToString()
