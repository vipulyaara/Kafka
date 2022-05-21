package com.kafka.data.model.item

import kotlinx.serialization.SerialName

data class Params(
    @SerialName("fields")
    val fields: String,
    @SerialName("json.wrf")
    val jsonwrf: String,
    @SerialName("qin")
    val qin: String,
    @SerialName("query")
    val query: String,
    @SerialName("rows")
    val rows: String,
    @SerialName("start")
    val start: Int,
    @SerialName("wt")
    val wt: String
)
