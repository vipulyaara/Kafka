package com.kafka.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "review")
@Serializable
data class Review(
    @PrimaryKey @ColumnInfo(name = "review_id")
    @SerialName("review_id") val reviewId: String,
    @ColumnInfo(name = "chapter_id")
    @SerialName("chapter_id") val chapterId: String?,
    @ColumnInfo(name = "item_id")
    @SerialName("item_id") val itemId: String,
    @ColumnInfo(name = "user_id")
    @SerialName("user_id") val userId: String,
    @ColumnInfo(name = "text")
    @SerialName("text") val text: String,
    @ColumnInfo(name = "rating")
    @SerialName("rating") val rating: Float,
    @ColumnInfo(name = "created_at")
    @SerialName("created_at") val createdAt: Instant
): BaseEntity

data class Comment(
    val commentId: String,
    val reviewId: String,
    val itemId: String,
    val text: String,
    val createdAt: Instant
)
