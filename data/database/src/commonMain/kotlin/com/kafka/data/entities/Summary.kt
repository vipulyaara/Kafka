package com.kafka.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    @SerialName("item_id") val itemId: String,
    @SerialName("text") val content: String,
)
