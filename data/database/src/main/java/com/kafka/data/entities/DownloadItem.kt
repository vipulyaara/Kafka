package com.kafka.data.entities

import com.google.firebase.firestore.DocumentId

data class DownloadItem(
    val itemId: String = "",
    val fileId: String = "",
    @DocumentId
    val id: String = "",
    val downloadUrl: String = "",
) : BaseEntity

