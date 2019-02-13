@file:Suppress("ObjectPropertyName")

package com.airtel.data.query

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

data class ArchiveQuery(
    val queries: HashMap<String, String> = hashMapOf()
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
