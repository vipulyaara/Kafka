package com.kafka.kms.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GutenbergBook(
    val id: Int,
    val title: String,
    val authors: List<Author>,
    val languages: List<String>,
    @SerialName("download_count")
    val downloadCount: Int,
    val formats: Formats
)

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
    val applicationPdf: String? = null
)
