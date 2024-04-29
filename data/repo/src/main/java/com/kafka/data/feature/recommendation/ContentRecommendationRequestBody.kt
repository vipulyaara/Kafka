package com.kafka.data.feature.recommendation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentRecommendationRequestBody(
    @SerialName("content_object_type")
    val contentObjectType: String,
    @SerialName("content_tag_object_type")
    val contentTagObjectType: String,
    @SerialName("content_tagged_relationship_type")
    val contentTaggedRelationshipType: String,
    @SerialName("object")
    val objectX: Object,
    @SerialName("primary_positive_relationship_type")
    val primaryPositiveRelationshipType: String,
    @SerialName("secondary_positive_relationship_type")
    val secondaryPositiveRelationshipType: String
) {
    @Serializable
    data class Object(
        @SerialName("id")
        val id: String,
        @SerialName("type")
        val type: String
    )

    companion object {
        fun fromUser(userId: String) = ContentRecommendationRequestBody(
            contentObjectType = "content",
            primaryPositiveRelationshipType = "liked",
            secondaryPositiveRelationshipType = "viewed",
            contentTaggedRelationshipType = "taggedWith",
            contentTagObjectType = "tag",
            objectX = Object(id = userId, type = "user")
        )
    }
}
