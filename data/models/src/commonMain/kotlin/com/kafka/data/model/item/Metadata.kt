package com.kafka.data.model.item

import com.kafka.data.model.StringListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    @SerialName("creator")
    @Serializable(with = StringListSerializer::class)
    val creator: List<String>? = null,
    @SerialName("collection")
    @Serializable(with = StringListSerializer::class)
    val collection: List<String>? = null,
    @SerialName("description")
    @Serializable(with = StringListSerializer::class)
    val description: List<String>? = null,
    @SerialName("subject")
    @Serializable(with = StringListSerializer::class)
    val subject: List<String>? = null,
    @SerialName("identifier")
    val identifier: String,
    @SerialName("mediatype")
    val mediatype: String? = null,
    @SerialName("title")
    @Serializable(with = StringListSerializer::class)
    val title: List<String>? = null,
    @SerialName("language")
    @Serializable(with = StringListSerializer::class)
    val languages: List<String>? = null,
    @SerialName("access-restricted-item")
    val accessRestrictedItem: Boolean = false,
)
