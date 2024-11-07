package com.kafka.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feedback(
    @SerialName("email") val email: String?,
    @SerialName("text") val text: String
)

@Serializable
data class Report(
    @SerialName("email") val email: String,
    @SerialName("type") val type: String,
    @SerialName("text") val text: String
)
