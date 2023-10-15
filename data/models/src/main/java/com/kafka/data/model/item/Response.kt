package com.kafka.data.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    @SerialName("docs")
    val docs: List<Doc>,
    @SerialName("numFound")
    val numFound: Int,
    @SerialName("start")
    val start: Int,
)
