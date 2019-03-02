@file:Suppress("ObjectPropertyName")

package com.airtel.data.query

import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf

/**
 * @author Vipul Kumar; dated 13/02/19.
 */

const val _mediaType = "mediaType"
const val _mediaTypeText = "texts"
const val _books = "texts"
const val _audio = "audio"
const val _image = "image"
const val _video = "video"
const val _collection = "collection"
const val _railTitle = "railtitle"

const val _creator = "creator"
const val _genre = "genre"
const val _searchTerm = ""

data class ArchiveQuery(
    var title: String? = null,
    val queries: ArrayMap<String, String> = arrayMapOf()
)

fun ArchiveQuery.booksByCollection(collection: String?): ArchiveQuery {
    title = "Books by $collection"
    queries[_mediaType] = _audio
    queries[_collection] = collection
    return this
}

fun ArchiveQuery.searchByKeyword(keyword: String?): ArchiveQuery {
    title = "Books by $keyword"
    queries[_mediaType] = _audio
    queries[_searchTerm] = keyword
    return this
}

fun ArchiveQuery.booksByAuthor(author: String?): ArchiveQuery {
    title = "Books by $author"
    queries[_mediaType] = _audio
    queries[_creator] = author
    return this
}

fun ArchiveQuery.booksByGenre(genre: String?): ArchiveQuery {
    title = "Books in $genre"
    queries[_mediaType] = _audio
    queries[_genre] = genre
    return this
}

fun ArchiveQuery.buildSearchTerm(): String {
    var query = ""
    for ((key, value) in queries) {
        query = query.plus("$key:($value)+AND+")
    }
    return query.dropLast(5)
}
