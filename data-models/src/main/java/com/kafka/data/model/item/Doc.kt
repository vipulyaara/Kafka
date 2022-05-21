package com.kafka.data.model.item

import kotlinx.serialization.SerialName

data class Doc(
    @SerialName("backup_location")
    val backupLocation: String,
    @SerialName("btih")
    val btih: String,
    @SerialName("call_number")
    val callNumber: String,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("genre")
    val collection: List<String>,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("contributor")
    val contributor: List<String>,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("genre")
    val creator: List<String>?,
    @SerialName("date")
    val date: String,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("description")
    val description: List<String>?,
    @SerialName("downloads")
    val downloads: Int,
    @SerialName("external-identifier")
    val externalIdentifier: String,
    @SerialName("foldoutcount")
    val foldoutcount: Int,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("format")
    val format: List<String>,
    @SerialName("identifier")
    val identifier: String,
    @SerialName("imagecount")
    val imagecount: Int,
    @SerialName("indexflag")
    val indexflag: List<String>,
    @SerialName("item_size")
    val itemSize: Int,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("language")
    val language: List<String>,
    @SerialName("mediatype")
    val mediatype: String,
    @SerialName("month")
    val month: Int,
    @SerialName("oai_updatedate")
    val oaiUpdatedate: List<String>,
//    @SerialName("publicdate")
//    val publicdate: String,
//    @SerialName("publisher")
//    val publisher: String,
    @SerialName("stripped_tags")
    val strippedTags: String,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("subject")
    val subject: List<String>,
//    @com.kafka.data.model.mapper.SingleToArray
    @SerialName("title")
    val title: List<String>,
    @SerialName("week")
    val week: Int,
    @SerialName("year")
    val year: String
)
