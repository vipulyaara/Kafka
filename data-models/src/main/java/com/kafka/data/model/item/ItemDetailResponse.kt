package com.kafka.data.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDetailResponse(
    @SerialName("created")
    val created: Int,
    @SerialName("d1")
    val d1: String,
    @SerialName("dir")
    val dir: String,
    @SerialName("files")
    val files: List<File>,
    @SerialName("files_count")
    val filesCount: Int,
    @SerialName("item_size")
    val itemSize: Long,
    @SerialName("metadata")
    val metadata: Metadata,
    @SerialName("server")
    val server: String,
    @SerialName("uniq")
    val uniq: Int,
    @SerialName("workable_servers")
    val workableServers: List<String>
)
