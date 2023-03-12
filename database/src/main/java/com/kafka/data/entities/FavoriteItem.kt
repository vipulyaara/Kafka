package com.kafka.data.entities

import com.google.firebase.firestore.DocumentId

data class FavoriteItem(
    @DocumentId
    val itemId: String = "",
    val title: String = "",
    val creator: String = "",
    val mediaType: String = "",
    val coverImage: String = "",
) : BaseEntity
