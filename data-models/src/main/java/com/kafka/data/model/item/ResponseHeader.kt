package com.kafka.data.model.item

import kotlinx.serialization.SerialName

data class ResponseHeader(
    @SerialName("QTime")
    val qTime: Int,
    @SerialName("params")
    val params: Params,
    @SerialName("status")
    val status: Int
)
