package com.kafka.data.model.item

import com.data.base.mapper.SingleToArray
import com.squareup.moshi.Json

data class Metadata(
    @Json(name = "addeddate")
    val addeddate: String?,
    @Json(name = "backup_location")
    val backupLocation: String?,
    @Json(name = "boxid")
    val boxid: String?,
//    @Json(name = "genre")
//    val genre: List<String?>,
    @SingleToArray
    @Json(name = "creator")
    val creator: List<String>?,
    @Json(name = "curation")
    val curation: String?,
    @Json(name = "date")
    val date: String?,
    @SingleToArray
    @Json(name = "description")
    val description: List<String>?,
    @Json(name = "identifier")
    val identifier: String,
    @Json(name = "licenseurl")
    val licenseurl: String?,
    @Json(name = "mediatype")
    val mediatype: String?,
    @Json(name = "publicdate")
    val publicdate: String?,
    @Json(name = "runtime")
    val runtime: String?,
    @Json(name = "source")
    val source: String?,
//    @Json(name = "subject")
//    val subject: String?,
    @Json(name = "taper")
    val taper: String?,
    @Json(name = "title")
    val title: String?,
//    @Json(name = "updatedate")
//    val updatedate: List<String?>,
//    @Json(name = "updater")
//    val updater: List<String?>,
//    @Json(name = "uploader")
//    val uploader: String?,
    @Json(name = "year")
    val year: String?
)
