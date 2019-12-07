@file:Suppress("ObjectPropertyName")

package com.kafka.data.query

import androidx.collection.ArrayMap

/**
 * @author Vipul Kumar; dated 13/02/19.
 */

const val _mediaType = "mediaType"
const val _mediaTypeText = "texts"
const val _books = "texts"
const val _audio = "audio"
const val _image = "image"
const val _video = "video"
const val _librivoxaudio = "librivoxaudio"
const val _collection = "collection"
const val _railTitle = "railtitle"

const val _creator = "creator"
const val _genre = "genre"
const val _searchTerm = ""

data class ArchiveQuery(
    var title: String? = null,
    val queries: ArrayMap<String, String> = ArrayMap()
)

fun ArchiveQuery.booksByCollection(collection: String?): ArchiveQuery {
    title = "Books by $collection"
    queries.put(_mediaType, _audio)
    queries.put(_collection, collection)
    return this
}

fun ArchiveQuery.searchByKeyword(keyword: String?): ArchiveQuery {
    title = "Books by $keyword"
    queries.put(_mediaType, _audio)
    queries.put(_searchTerm, keyword)
    return this
}

fun ArchiveQuery.booksByAuthor(author: String?): ArchiveQuery {
    title = "Books by $author"
    queries.put(_mediaType, _audio)
    queries.put(_creator, author)
    return this
}

fun ArchiveQuery.booksByGenre(genre: String?): ArchiveQuery {
    title = "Books in $genre"
    queries.put(_mediaType, _audio)
    queries.put(_genre, genre)
    return this
}

fun ArchiveQuery.buildSearchTerm(): String {
    var query = ""
    queries.put(_collection, _librivoxaudio)
    for ((key, value) in queries.entries) {
        query = query.plus("$key:($value)+AND+")
    }
    return query.dropLast(5)
}
