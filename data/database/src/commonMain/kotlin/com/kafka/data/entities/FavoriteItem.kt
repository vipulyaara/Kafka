package com.kafka.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteItem(
    @SerialName("item_id") val itemId: String,
    @SerialName("uid") val uid: String,
)

const val listIdFavorites = "items"
