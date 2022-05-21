package com.kafka.data.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseHeader(
    @SerialName("QTime")
    val qTime: Int,
    @SerialName("params")
    val params: Params,
    @SerialName("status")
    val status: Int
)
