package com.kafka.data.entities

import androidx.annotation.Keep

@Keep
data class User(
    val id: String,
    val displayName: String,
    val email: String?,
    val imageUrl: String? = null,
    val anonymous: Boolean,
) {
    constructor() : this("", "", null, null, false)
}
