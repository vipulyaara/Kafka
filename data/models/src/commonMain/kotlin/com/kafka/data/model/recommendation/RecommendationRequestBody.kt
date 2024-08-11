package com.kafka.data.model.recommendation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationRequestBody(
    @SerialName("event")
    val event: RecommendationEvent
) {
    @Serializable
    data class RecommendationEvent(
        @SerialName("start_object")
        val startObject: StartObject,
        @SerialName("relationship")
        val relationship: Relationship,
        @SerialName("end_object")
        val endObject: EndObject,
    ) {
        @Serializable
        data class EndObject(
            @SerialName("created_on")
            val createdOn: String,
            @SerialName("id")
            val id: String,
            @SerialName("type")
            val type: String
        )

        @Serializable
        data class Relationship(
            @SerialName("created_on")
            val createdOn: String,
            @SerialName("type")
            val type: String
        )

        @Serializable
        data class StartObject(
            @SerialName("created_on")
            val createdOn: String,
            @SerialName("id")
            val id: String,
            @SerialName("type")
            val type: String
        )
    }
}
