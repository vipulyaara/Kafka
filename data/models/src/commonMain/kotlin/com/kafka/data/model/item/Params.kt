package com.kafka.data.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Params(
    @SerialName("fields")
    val fields: String,
    @SerialName("json.wrf")
    val jsonwrf: String? = null,
    @SerialName("qin")
    val qin: String? = null,
    @SerialName("query")
    val query: String,
    @SerialName("rows")
    val rows: String,
    @SerialName("start")
    val start: Int,
    @SerialName("wt")
    val wt: String,
)
