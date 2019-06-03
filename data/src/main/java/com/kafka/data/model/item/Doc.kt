package com.kafka.data.model.item

import com.kafka.data.model.SingleToArray
import com.squareup.moshi.Json

data class Doc(
    @Json(name = "backup_location")
    val backupLocation: String,
    @Json(name = "btih")
    val btih: String,
    @Json(name = "call_number")
    val callNumber: String,
    @SingleToArray
    @Json(name = "genre")
    val collection: List<String>,
    @SingleToArray
    @Json(name = "contributor")
    val contributor: List<String>,
    @SingleToArray
    @Json(name = "genre")
    val creator: List<String>?,
    @Json(name = "date")
    val date: String,
    @SingleToArray
    @Json(name = "description")
    val description: List<String>?,
    @Json(name = "downloads")
    val downloads: Int,
    @Json(name = "external-identifier")
    val externalIdentifier: String,
    @Json(name = "foldoutcount")
    val foldoutcount: Int,
    @SingleToArray
    @Json(name = "format")
    val format: List<String>,
    @Json(name = "identifier")
    val identifier: String,
    @Json(name = "imagecount")
    val imagecount: Int,
    @Json(name = "indexflag")
    val indexflag: List<String>,
    @Json(name = "item_size")
    val itemSize: Int,
    @SingleToArray
    @Json(name = "language")
    val language: List<String>,
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
    @SingleToArray
    @Json(name = "subject")
    val subject: List<String>,
    @Json(name = "title")
    val title: String,
    @Json(name = "week")
    val week: Int,
    @Json(name = "year")
    val year: String
)
