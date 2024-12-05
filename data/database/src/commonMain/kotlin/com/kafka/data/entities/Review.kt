package com.kafka.data.entities

import kotlinx.datetime.Instant

data class Review(
    val reviewId: String,
    val chapterId: String,
    val itemId: String,
    val userId: String,
    val text: String,
    val rating: Float,
    val createdAt: Instant
)

data class Comment(
    val commentId: String,
    val reviewId: String,
    val itemId: String,
    val text: String,
    val createdAt: Instant
)