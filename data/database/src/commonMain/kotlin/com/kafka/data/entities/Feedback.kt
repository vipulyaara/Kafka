package com.kafka.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feedback(
    @SerialName("email") val email: String?,
    @SerialName("text") val text: String
)
