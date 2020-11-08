package com.kafka.data.model.model.item

import com.squareup.moshi.Json

data class Params(
    @Json(name = "fields")
    val fields: String,
    @Json(name = "json.wrf")
    val jsonwrf: String,
    @Json(name = "qin")
    val qin: String,
    @Json(name = "query")
    val query: String,
    @Json(name = "rows")
    val rows: String,
    @Json(name = "start")
    val start: Int,
    @Json(name = "wt")
    val wt: String
)
