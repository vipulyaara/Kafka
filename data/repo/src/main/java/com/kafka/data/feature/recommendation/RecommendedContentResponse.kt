package com.kafka.data.feature.recommendation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendedContentResponse(
    @SerialName("items")
    val items: List<Item>
) {
    @Serializable
    data class Item(
        @SerialName("object")
        val objectX: Object
    ) {
        @Serializable
        data class Object(
            @SerialName("created_on")
            val createdOn: String,
            @SerialName("id")
            val id: String,
            @SerialName("modified_on")
            val modifiedOn: String,
            @SerialName("type")
            val type: String
        )
    }
}
