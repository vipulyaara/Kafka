package com.kafka.data.entities

import com.google.firebase.firestore.DocumentId

data class DownloadItem(
    @DocumentId
    val id: String = "",
    val fileId: String = "",
    val downloadUrl: String = "",
    val itemId: String = "",
    val fileTitle: String = "",
    val itemTitle: String = "",
    val creator: String = "",
    val mediaType: String = "",
    val coverImage: String = "",
)
