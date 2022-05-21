package com.kafka.data.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("response")
    val response: Response,
    @SerialName("responseHeader")
    val responseHeader: ResponseHeader
)
