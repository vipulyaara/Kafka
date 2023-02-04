package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class TextFile(
    @PrimaryKey val id: String,
    val itemId: String,
    val title: String,
    val localUri: String,
    val pages: List<Page>,
    val totalPages: Int,
    val currentPage: Int,
) : BaseEntity {
    @Serializable
    data class Page(val index: Int, val text: String)

    val type: Type
        get() = when {
            pages.isEmpty() -> Type.PDF
            else -> Type.TXT
        }

    enum class Type { PDF, TXT, }
}
