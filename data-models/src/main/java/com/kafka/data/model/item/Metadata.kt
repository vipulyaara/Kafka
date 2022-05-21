package com.kafka.data.model.item

import kotlinx.serialization.SerialName

data class Metadata(
    @SerialName("addeddate")
    val addeddate: String?,
    @SerialName("backup_location")
    val backupLocation: String?,
    @SerialName("boxid")
    val boxid: String?,
//    @SerialName("genre")
//    val genre: List<String?>,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("creator")
    val creator: List<String>?,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("collection")
    val collection: List<String>?,
    @SerialName("curation")
    val curation: String?,
    @SerialName("date")
    val date: String?,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("description")
    val description: List<String>?,
    @SerialName("identifier")
    val identifier: String,
    @SerialName("licenseurl")
    val licenseurl: String?,
    @SerialName("mediatype")
    val mediatype: String?,
    @SerialName("publicdate")
    val publicdate: String?,
    @SerialName("runtime")
    val runtime: String?,
    @SerialName("source")
    val source: String?,
//    @SerialName("subject")
//    val subject: String?,
    @SerialName("taper")
    val taper: String?,
    @SerialName("title")
    val title: String?,
//    @SerialName("updatedate")
//    val updatedate: List<String?>,
//    @SerialName("updater")
//    val updater: List<String?>,
//    @SerialName("uploader")
//    val uploader: String?,
    @SerialName("year")
    val year: String?
)
