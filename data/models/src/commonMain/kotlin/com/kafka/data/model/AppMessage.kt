package com.kafka.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppMessage(
    @SerialName("id") val id: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("text") val text: String = "",
    @SerialName("image") val image: String = "",
    @SerialName("enabled") val enabled: Boolean = false,
    @SerialName("primary_url") val primaryUrl: String = "",
    @SerialName("primary_call_to_action") val primaryAction: String = "",
    @SerialName("secondary_call_to_action") val secondaryAction: String = "",
    @SerialName("countries") val countries: List<String> = emptyList(),
    @SerialName("type") val type: Type,
) {
    enum class Type { Soft, Hard }

    val snackbarMessage: String
        get() = title.ifEmpty { text }
}
