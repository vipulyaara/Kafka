package com.kafka.data.entities

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class Summary(
    @SerialName("item_id") val itemId: String,
    @SerialName("content") val content: String,
) {
    constructor() : this("", "")
}
