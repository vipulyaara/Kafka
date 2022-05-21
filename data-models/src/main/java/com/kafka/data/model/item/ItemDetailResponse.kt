package com.kafka.data.model.item

import kotlinx.serialization.SerialName

data class ItemDetailResponse(
    @SerialName("created")
    val created: Int,
    @SerialName("d1")
    val d1: String,
    @SerialName("d2")
    val d2: String,
    @SerialName("dir")
    val dir: String,
    @SerialName("files")
    val files: List<File>,
    @SerialName("files_count")
    val filesCount: Int,
    @SerialName("item_size")
    val itemSize: Int,
    @SerialName("metadata")
    val metadata: Metadata,
    @SerialName("server")
    val server: String,
    @SerialName("uniq")
    val uniq: Int,
    @SerialName("workable_servers")
    val workableServers: List<String>
)
