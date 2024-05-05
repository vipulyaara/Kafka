package com.kafka.data.feature.recommendation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RelatedContentRequestBody(
    @SerialName("content_tagged_relationship_type")
    val contentTaggedRelationshipType: String,
    @SerialName("object")
    val objectX: Object,
    @SerialName("positive_rel")
    val primaryPositiveRelationshipType: String
) {
    @Serializable
    data class Object(
        @SerialName("id")
        val id: String,
        @SerialName("type")
        val type: String
    )

    companion object {
        fun fromContent(contentId: String) = RelatedContentRequestBody(
            primaryPositiveRelationshipType = "viewed",
            contentTaggedRelationshipType = "taggedWith",
            objectX = Object(id = contentId, type = "content")
        )
    }
}
