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
const val _mediaTypeAudio = "audio"
const val _image = "image"
const val _video = "video"

const val _creator = "creator"
const val _genre = "genre"
const val _searchTerm = ""

data class ArchiveQuery(
    val queries: ArrayMap<String, String> = arrayMapOf()
)

fun ArchiveQuery.booksByAuthor(author: String): ArchiveQuery {
    queries[_mediaType] = _books
    queries[_creator] = author
    return this
}

fun ArchiveQuery.booksByGenre(genre: String): ArchiveQuery {
    queries[_mediaType] = _books
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
