package com.kafka.data.entities

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
@Keep
data class Summary(
    @SerialName("item_id") val itemId: String,
    @SerialName("content") val content: String,
) {
    constructor() : this("", "")
}
