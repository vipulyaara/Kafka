@file:Suppress("ObjectPropertyName")

package com.data.base.model

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
const val _mediaType = "mediaType"
const val _mediaTypeText = "texts"
const val _mediaTypeAudio = "audio"
const val _librivoxaudio = "librivoxaudio"
const val _identifier = "identifier"
const val _collection = "collection"

const val _creator = "creator_name"
const val _creator_remote = "creator"
const val _title = "title"

const val joinerAnd = "AND"
const val joinerOr = "OR"

data class QueryItem(val key: String, val value: String, val joiner: String = "")

data class ArchiveQuery(
    var queries: MutableList<QueryItem> = mutableListOf(),
    var isOrderBy: Boolean = true
)

fun ArchiveQuery.booksByIdentifiers(identifiers: String): ArchiveQuery {
    identifiers.split(", ").forEach {
        queries.add(QueryItem(_identifier, it, joinerOr))
    }
    queries = queries.mapIndexed { index, queryItem ->
        if (index == queries.lastIndex) queryItem.copy(joiner = "") else queryItem
    }.toMutableList()

    isOrderBy = false

    return this
}

fun ArchiveQuery.booksByCollection(collection: String): ArchiveQuery {
    queries.add(QueryItem(_collection, collection))
    return this
}

fun ArchiveQuery.booksByTitleKeyword(keyword: String): ArchiveQuery {
    queries.add(QueryItem(_title, keyword))
    return this
}

fun ArchiveQuery.booksByAuthor(author: String): ArchiveQuery {
    queries.add(QueryItem(_creator, author))
    return this
}

fun ArchiveQuery.booksByTitleOrCreator(keyword: String): ArchiveQuery {
    queries.add(QueryItem(_title, keyword, joinerOr))
    queries.add(QueryItem(_creator, keyword))
    return this
}
