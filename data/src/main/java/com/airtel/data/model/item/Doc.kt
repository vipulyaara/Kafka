package com.airtel.data.model.item

import com.squareup.moshi.Json

data class Doc(
    @Json(name = "backup_location")
    val backupLocation: String,
    @Json(name = "btih")
    val btih: String,
    @Json(name = "call_number")
    val callNumber: String,
    @Json(name = "collection")
    val collection: List<String>,
    @Json(name = "contributor")
    val contributor: String,
    @Json(name = "creator")
    val creator: String,
    @Json(name = "date")
    val date: String,
//    @Json(name = "description")
//    val description: String,
    @Json(name = "downloads")
    val downloads: Int,
    @Json(name = "external-identifier")
    val externalIdentifier: String,
    @Json(name = "foldoutcount")
    val foldoutcount: Int,
//    @Json(name = "format")
//    val format: List<String>,
    @Json(name = "identifier")
    val identifier: String,
    @Json(name = "imagecount")
    val imagecount: Int,
    @Json(name = "indexflag")
    val indexflag: List<String>,
    @Json(name = "item_size")
    val itemSize: Int,
//    @Json(name = "language")
//    val language: String,
    @Json(name = "mediatype")
    val mediatype: String,
    @Json(name = "month")
    val month: Int,
    @Json(name = "oai_updatedate")
    val oaiUpdatedate: List<String>,
//    @Json(name = "publicdate")
//    val publicdate: String,
//    @Json(name = "publisher")
//    val publisher: String,
    @Json(name = "stripped_tags")
    val strippedTags: String,
//    @Json(name = "subject")
//    val subject: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "week")
    val week: Int,
    @Json(name = "year")
    val year: String
)
