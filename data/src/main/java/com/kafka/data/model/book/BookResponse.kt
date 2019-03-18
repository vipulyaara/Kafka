package com.kafka.data.model.book

import com.squareup.moshi.Json

@Suppress("SpellCheckingInspection")
data class BookResponse(
    @Json(name = "authors")
    val authors: List<AuthorResponse>,
    @Json(name = "copyright_year")
    val copyrightYear: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "language")
    val language: String,
    @Json(name = "num_sections")
    val numSections: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "totaltime")
    val totalTime: String,
    @Json(name = "totaltimesecs")
    val totalTimeSecs: Int,
    @Json(name = "url_librivox")
    val urlLibrivox: String,
    @Json(name = "url_other")
    val urlOther: Any?,
    @Json(name = "url_project")
    val urlProject: String,
    @Json(name = "url_rss")
    val urlRss: String,
    @Json(name = "url_text_source")
    val urlTextSource: String,
    @Json(name = "url_zip_file")
    val urlZipFile: String
)
