package com.kafka.data.entities

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FavoriteItem(
    @DocumentId
    val itemId: String = "",
    val title: String = "",
    val creator: String = "",
    val mediaType: String = "",
    val coverImage: String = "",
    @ServerTimestamp
    val createdAt: Date = Date(),
) : BaseEntity

fun FavoriteItem.toItem() = Item(
    itemId = itemId,
    title = title,
    creator = Creator(id = creator, name = creator),
    mediaType = mediaType,
    coverImage = coverImage,
)
