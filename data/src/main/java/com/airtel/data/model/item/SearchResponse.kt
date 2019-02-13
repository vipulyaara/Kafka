package com.airtel.data.model.item

import com.airtel.data.model.item.ResponseHeader
import com.squareup.moshi.Json

data class SearchResponse(
    @Json(name = "response")
    val response: Response,
    @Json(name = "responseHeader")
    val responseHeader: ResponseHeader
)
