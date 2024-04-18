package com.kafka.data.model.item

import com.kafka.data.model.StringListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    @SerialName("addeddate")
    val addeddate: String? = null,
    @SerialName("creator")
    @Serializable(with = StringListSerializer::class)
    val creator: List<String>? = null,
    @SerialName("collection")
    @Serializable(with = StringListSerializer::class)
    val collection: List<String>? = null,
    @SerialName("date")
    @Serializable(with = StringListSerializer::class)
    val date: List<String>? = null,
    @SerialName("description")
    @Serializable(with = StringListSerializer::class)
    val description: List<String>? = null,
    @SerialName("subject")
    @Serializable(with = StringListSerializer::class)
    val subject: List<String>? = null,
    @SerialName("identifier")
    val identifier: String,
    @SerialName("licenseurl")
    val licenseurl: String? = null,
    @SerialName("mediatype")
    val mediatype: String? = null,
    @SerialName("publicdate")
    val publicdate: String? = null,
    @SerialName("runtime")
    val runtime: String? = null,
    @SerialName("source")
    val source: String? = null,
    @SerialName("taper")
    val taper: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("year")
    val year: String? = null,
    @SerialName("language")
    @Serializable(with = StringListSerializer::class)
    val languages: List<String>? = null,
)
