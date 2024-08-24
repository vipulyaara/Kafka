package com.kafka.data.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDetailResponse(
    @SerialName("dir")
    val dir: String,
    @SerialName("files")
    val files: List<File>,
    @SerialName("files_count")
    val filesCount: Int = 0,
    @SerialName("metadata")
    val metadata: Metadata,
    @SerialName("server")
    val server: String = "",
)
