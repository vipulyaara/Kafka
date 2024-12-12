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
    @PrimaryKey @ColumnInfo(name = "review_id") @SerialName("review_id") val reviewId: String,
    @ColumnInfo(name = "item_id") @SerialName("item_id") val itemId: String,
    @ColumnInfo(name = "user_id") @SerialName("user_id") val userId: String,
    @ColumnInfo(name = "user_name") @SerialName("user_name") val userName: String,
    @ColumnInfo(name = "text") @SerialName("text") val text: String,
    @ColumnInfo(name = "rating") @SerialName("rating") val rating: Float,
    @ColumnInfo(name = "likes") @SerialName("likes") val likes: Int,
    @ColumnInfo(name = "dislikes") @SerialName("dislikes") val dislikes: Int,
    @ColumnInfo(name = "created_at") @SerialName("created_at") val createdAt: Instant
): BaseEntity

enum class Reaction { Like, Dislike }

data class Comment(
    val commentId: String,
    val reviewId: String,
    val itemId: String,
    val text: String,
    val createdAt: Instant
)
