package com.kafka.kms.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GutenbergBook(
    val id: Int,
    val title: String,
    val authors: List<Author>,
    val translators: List<Author>,
    val languages: List<String>,
    val subjects: List<String>,
    val bookshelves: List<String>,
    val copyright: Boolean,
    @SerialName("download_count")
    val downloadCount: Int,
    val formats: Formats
) {
    val itemId = "gutenberg_$id"
    val octetFileId = "${itemId}_octet"
}

@Serializable
data class Author(
    val name: String,
    @SerialName("birth_year")
    val birthYear: Int? = null,
    @SerialName("death_year")
    val deathYear: Int? = null
)

@Serializable
data class Formats(
    @SerialName("text/plain")
    val textPlain: String? = null,
    @SerialName("text/plain; charset=utf-8")
    val textPlainUtf8: String? = null,
    @SerialName("text/html")
    val textHtml: String? = null,
    @SerialName("application/pdf")
    val applicationPdf: String? = null,
    @SerialName("application/epub+zip")
    val applicationEpub: String? = null,
    @SerialName("application/octet-stream")
    val octetStream: String? = null
)
