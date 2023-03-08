package com.kafka.data.entities

import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.DocumentId
import com.kafka.data.model._mediaTypeAudio
import com.kafka.data.model._mediaTypeText

@Keep
sealed class RecentItem {
    abstract val fileId: String
    abstract val itemId: String
    abstract val title: String
    abstract val coverUrl: String
    abstract val creator: String
    abstract val mediaType: String
    abstract val createdAt: Long

    @Keep
    data class Readable(
        @DocumentId
        override val fileId: String,
        override val itemId: String,
        override val title: String,
        override val coverUrl: String,
        override val creator: String,
        override val mediaType: String,
        override val createdAt: Long,
        val currentPage: Int,
        val localUri: String,
//        val pages: List<Page>,
    ) : RecentItem() {
        constructor(): this("", "", "", "", "", "", 0, 0, "")

//        @Keep
//        data class Page(val number: Int, val text: String = "")
    }

    @Keep
    data class Listenable(
        @DocumentId
        override val fileId: String,
        override val itemId: String,
        override val title: String,
        override val coverUrl: String,
        override val creator: String,
        override val mediaType: String,
        override val createdAt: Long,
        val currentTimeStamp: Long
    ) : RecentItem() {
        constructor(): this("", "", "", "", "", "", 0, 0)
    }

    companion object {
        fun fromItem(item: ItemDetail): RecentItem {
            return when (item.mediaType) {
                _mediaTypeText -> {
                    Readable(
                        fileId = item.primaryTextFile!!,
                        itemId = item.itemId,
                        title = item.title.orEmpty(),
                        coverUrl = item.coverImage.orEmpty(),
                        creator = item.creator.orEmpty(),
                        mediaType = item.mediaType,
                        createdAt = System.currentTimeMillis(),
                        currentPage = 0,
                        localUri = ""
                    )
                }
                _mediaTypeAudio -> {
                    Listenable(
                        fileId = item.files!!.first(),
                        itemId = item.itemId,
                        title = item.title.orEmpty(),
                        coverUrl = item.coverImage.orEmpty(),
                        creator = item.creator.orEmpty(),
                        mediaType = item.mediaType,
                        createdAt = System.currentTimeMillis(),
                        currentTimeStamp = 0
                    )
                }
                else -> error("Unsupported media type: ${item.mediaType}")
            }
        }
    }
}
