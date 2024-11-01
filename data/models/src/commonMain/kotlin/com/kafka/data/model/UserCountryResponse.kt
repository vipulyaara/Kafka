package com.kafka.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCountryResponse(
    @SerialName("country")
    val country: String
)
