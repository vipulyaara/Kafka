package com.kafka.data.entities

import com.kafka.data.entities.Bookshelf.Type
import com.kafka.data.entities.Bookshelf.Visibility
import com.kafka.data.model.MediaType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bookshelf(
    @SerialName("bookshelf_id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("type") val type: Type,
    @SerialName("visibility") val visibility: Visibility,
    @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
) {
    enum class Type { Favorite, Reading, Completed, Wishlist, Uploads, Custom }
    enum class Visibility { Public, Private }
}

object BookshelfDefaults {
    val favorites = Bookshelf("favorites", "Favorites", Type.Favorite, Visibility.Public)
    val wishlist = Bookshelf("wishlist", "Wishlist", Type.Wishlist, Visibility.Public)
    val reading = Bookshelf("reading", "Reading", Type.Wishlist, Visibility.Public)
    val completed = Bookshelf("completed", "Completed", Type.Completed, Visibility.Public)
    val uploads = Bookshelf("uploads", "Uploads", Type.Uploads, Visibility.Private)

    val all = listOf(favorites, wishlist, reading, completed, uploads)
    val availableShelves = listOf(wishlist, uploads, favorites)
    val default = wishlist
}

@Serializable
data class BookshelfItem(
    @SerialName("item_id") val itemId: String,
    @SerialName("item_title") val itemTitle: String,
    @SerialName("creator") val creator: String,
    @SerialName("media_type") val mediaType: MediaType,
    @SerialName("cover_image") val coverImage: String,
    @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
) {
    companion object {
        fun ItemDetail.asBookshelfItem() = BookshelfItem(
            itemId = itemId,
            itemTitle = title,
            creator = creator.orEmpty(),
            mediaType = mediaType,
            coverImage = coverImage.orEmpty()
        )
    }
}
