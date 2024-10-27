package com.kafka.data.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteItem(
    @SerialName("item_id") val itemId: String,
    @SerialName("uid") val uid: String,
    @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
)
