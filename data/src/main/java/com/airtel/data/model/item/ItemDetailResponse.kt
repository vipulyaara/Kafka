package com.airtel.data.model.item

import com.squareup.moshi.Json

data class ItemDetailResponse(
    @Json(name = "created")
    val created: Int,
    @Json(name = "d1")
    val d1: String,
    @Json(name = "d2")
    val d2: String,
    @Json(name = "dir")
    val dir: String,
    @Json(name = "files")
    val files: List<File>,
    @Json(name = "files_count")
    val filesCount: Int,
    @Json(name = "item_size")
    val itemSize: Int,
    @Json(name = "metadata")
    val metadata: Metadata,
    @Json(name = "server")
    val server: String,
    @Json(name = "uniq")
    val uniq: Int,
    @Json(name = "workable_servers")
    val workableServers: List<String>
)
