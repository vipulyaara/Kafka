@file:Suppress("ObjectPropertyName")

package com.kafka.data.model

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
const val _mediaType = "mediaType"
const val _mediaTypeText = "texts"
const val _mediaTypeAudio = "audio"
const val _identifier = "identifier"
const val _collection = "collection"

const val _creator = "creator_name"
const val _subject = "subject"
const val _creator_remote = "creator"
const val _title = "title"

const val joinerAnd = "AND"
const val joinerOr = "OR"

data class QueryItem(val key: String, val value: String, val joiner: String = "")

data class ArchiveQuery(
    var queries: MutableList<QueryItem> = mutableListOf()
)

fun ArchiveQuery.booksByIdentifiers(identifiers: String): ArchiveQuery {
    return booksByIdentifiers(identifiers.split(", "))
}

fun ArchiveQuery.booksByIdentifiers(identifiers: List<String>): ArchiveQuery {
    identifiers.forEach {
        queries.add(QueryItem(_identifier, it, joinerOr))
    }
    queries = queries.mapIndexed { index, queryItem ->
        if (index == queries.lastIndex) queryItem.copy(joiner = "") else queryItem
    }.toMutableList()

    return this
}

fun ArchiveQuery.booksByCollection(collection: String, joiner: String = ""): ArchiveQuery {
    queries.add(QueryItem(_collection, collection, joiner))
    return this
}

fun ArchiveQuery.booksByTitleKeyword(keyword: String, joiner: String = ""): ArchiveQuery {
    queries.add(QueryItem(_title, keyword, joiner))
    return this
}

fun ArchiveQuery.booksByAuthor(author: String, joiner: String = ""): ArchiveQuery {
    queries.add(QueryItem(_creator, author, joiner))
    return this
}

fun ArchiveQuery.booksBySubject(subject: String, joiner: String = ""): ArchiveQuery {
    queries.add(QueryItem(_subject, subject, joiner))
    return this
}

fun ArchiveQuery.booksByTitleOrCreator(keyword: String): ArchiveQuery {
    queries.add(QueryItem(_title, keyword, joinerOr))
    queries.add(QueryItem(_creator, keyword))
    return this
}
