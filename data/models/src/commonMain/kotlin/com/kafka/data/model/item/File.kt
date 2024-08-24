package com.kafka.data.model.item

import com.kafka.data.model.StringListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class File(
    @SerialName("album")
    val album: String? = null,
    @SerialName("artist")
    val artist: String? = null,
    @Serializable(with = StringListSerializer::class)
    @SerialName("creator")
    val creator: List<String>? = null,
    @SerialName("format")
    val format: String? = null,
    @SerialName("genres")
    val genre: String? = null,
    @SerialName("length")
    val length: String? = null,
    @SerialName("name")
    val name: String,
    @Serializable(with = StringListSerializer::class)
    @SerialName("size")
    val size: List<String>? = null,
    @SerialName("title")
    val title: String? = null,
) {
    // todo: improve file ids to make them unique
    // this will require a migration to new file ids which might be a breaking change
    val fileId: String
        get() = (name + format).hashCode().toString()
}
