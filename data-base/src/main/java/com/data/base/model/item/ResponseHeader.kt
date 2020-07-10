package com.data.base.model.item

import com.squareup.moshi.Json

data class ResponseHeader(
    @Json(name = "QTime")
    val qTime: Int,
    @Json(name = "params")
    val params: Params,
    @Json(name = "status")
    val status: Int
)
