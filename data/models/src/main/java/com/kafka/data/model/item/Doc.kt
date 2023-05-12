package com.kafka.data.model.item

import com.kafka.data.model.StringListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Doc(
    @SerialName("genre")
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
    val downloads: Int,
    @SerialName("format")
    val format: List<String>? = null,
    @SerialName("identifier")
    val identifier: String,
    @SerialName("item_size")
    val itemSize: Long,
    @SerialName("language")
    @Serializable(with = StringListSerializer::class)
    val language: List<String>? = null,
    @SerialName("mediatype")
    val mediatype: String,
    @SerialName("month")
    val month: Int? = 0,
    @SerialName("subject")
    @Serializable(with = StringListSerializer::class)
    val subject: List<String>? = null,
    @SerialName("title")
    @Serializable(with = StringListSerializer::class)
    val title: List<String>,
    @SerialName("week")
    val week: Int? = 0,
    @SerialName("year")
    val year: String? = null,
    @SerialName("avg_rating")
    val rating: Double? = null,
)
