package com.kafka.data.feature.recommendation

enum class RecommendationRelationship {
    Viewed,
    Favorited,
    Used
}

sealed class RecommendationObject(val id: String, val key: String) {
    data class User(val userId: String) : RecommendationObject(userId, "user")
    data class Content(val contentId: String) : RecommendationObject(contentId, "content")
}
