package com.kafka.data.entities

import com.google.firebase.firestore.DocumentId
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteItem(
    @DocumentId
    val itemId: String = "",
    val title: String = "",
    val creator: String = "",
    val mediaType: String = "",
    val coverImage: String = "",
    val createdAt: Timestamp = Timestamp.now(),
)

fun FavoriteItem.toItem() = Item(
    itemId = itemId,
    title = title,
    creator = Creator(id = creator, name = creator),
    mediaType = mediaType,
    coverImage = coverImage,
)

const val listIdFavorites = "items"
