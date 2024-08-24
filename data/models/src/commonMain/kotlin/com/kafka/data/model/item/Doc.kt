package com.kafka.data.model.item

import com.kafka.data.model.StringListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Doc(
    @SerialName("collection")
    @Serializable(with = StringListSerializer::class)
    val collection: List<String>? = null,
    @SerialName("creator")
    @Serializable(with = StringListSerializer::class)
    val creator: List<String>? = null,
    @SerialName("date")
    @Serializable(with = StringListSerializer::class)
    val date: List<String>? = null,
    @SerialName("description")
    @Serializable(with = StringListSerializer::class)
    val description: List<String>? = null,
    @SerialName("downloads")
    val downloads: Long = 0,
    @SerialName("format")
    @Serializable(with = StringListSerializer::class)
    val format: List<String>? = null,
    @SerialName("identifier")
    val identifier: String,
    @SerialName("language")
    @Serializable(with = StringListSerializer::class)
    val language: List<String>? = null,
    @SerialName("mediatype")
    val mediatype: String,
    @SerialName("subject")
    @Serializable(with = StringListSerializer::class)
    val subject: List<String>? = null,
    @SerialName("title")
    @Serializable(with = StringListSerializer::class)
    val title: List<String>,
    @SerialName("avg_rating")
    val rating: Double? = null,
)
