package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

//interface RecentItem {
//    val id: String
//    val itemId: String
//    val createdAt: Long
//}

////@Entity
//data class RecentAudioItem(
//    override val id: String,
//    override val itemId: String,
//    override val createdAt: Long,
//    val currentAudio: Int,
//) : RecentItem {
//    data class Current(val fileId: String, val timestamp: Long)
//}

@Entity
data class RecentTextItem(
    @PrimaryKey val id: String,
    val itemId: String,
    val createdAt: Long,
    val title: String,
    val localUri: String,
    val pages: List<Page>,
    val currentPage: Int
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
