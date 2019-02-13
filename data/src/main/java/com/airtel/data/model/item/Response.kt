package com.airtel.data.model.item

import com.airtel.data.model.item.Doc
import com.squareup.moshi.Json

data class Response(
    @Json(name = "docs")
    val docs: List<Doc>,
    @Json(name = "numFound")
    val numFound: Int,
    @Json(name = "start")
    val start: Int
)
