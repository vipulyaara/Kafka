package com.kafka.data.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibrivoxFileResponse(
    @SerialName("sections")
    val sections: List<Section>
) {
    @Serializable
    data class Section(
        @SerialName("id")
        val id: String,
        @SerialName("title")
        val title: String,
        @SerialName("listen_url")
        val listenUrl: String,
        @SerialName("language")
        val language: String,
        @SerialName("playtime")
        val playtime: String,
        @SerialName("section_number")
        val sectionNumber: String,
    )
}
