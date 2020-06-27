@file:Suppress("ObjectPropertyName")

package com.kafka.data.query

import androidx.collection.ArrayMap
import androidx.sqlite.db.SimpleSQLiteQuery

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
const val _mediaType = "mediaType"
const val _mediaTypeText = "texts"
const val _books = "texts"
const val _mediatypeAudio = "audio"
const val _image = "image"
const val _video = "video"
const val _librivoxaudio = "librivoxaudio"
const val _collection = "collection"
const val _railTitle = "railtitle"

const val _creator = "creator"
const val _genre = "genre"

const val _searchTerm = ""

sealed class ResultTye {
    object Banner : ResultTye()
    object Row : ResultTye()
}

data class ArchiveQuery(
    var title: String? = null,
    val queries: ArrayMap<String, String> = ArrayMap(),
    val resultTye: ResultTye = ResultTye.Row,
    val position: Int = 0
)

fun ArchiveQuery.booksByCollection(collection: String?): ArchiveQuery {
    queries[_mediaType] = _mediatypeAudio
    queries[_collection] = collection
    return this
}

fun ArchiveQuery.searchByKeyword(keyword: String?): ArchiveQuery {
    queries[_mediaType] = _mediatypeAudio
    queries[_searchTerm] = keyword
    return this
}

fun ArchiveQuery.booksByAuthor(author: String?): ArchiveQuery {
//    queries[_mediaType] = _audio
    queries[_creator] = author
    return this
}

fun ArchiveQuery.booksByGenre(genre: String?): ArchiveQuery {
    queries[_mediaType] = _mediatypeAudio
    queries[_genre] = genre
    return this
}

fun ArchiveQuery.buildRemoteQuery(): String {
    var query = ""
    val joiner = "+AND+"
//    queries[_collection] = _librivoxaudio
    for ((key, value) in queries.entries) {
        query = query.plus("$key:($value)$joiner")
    }
    return query.removeSuffix(joiner)
}

fun ArchiveQuery.asLocalQuery() = SimpleSQLiteQuery(toQueryString())

fun ArchiveQuery.toQueryString(): String {
    val joiner = " AND "
    val selectFrom = "select * from item where"
    var where = " "
    queries.keys.first().let {
        where += "$it like '%${queries[it]?.replace(' ', '%')}%'$joiner"
    }
    val orderBy = " "
    return selectFrom + where.removeSuffix(joiner) + orderBy
}
