package com.kafka.data.entities

import com.kafka.data.entities.Bookshelf.Type
import com.kafka.data.entities.Bookshelf.Visibility
import com.kafka.data.model.MediaType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteItem(
    @SerialName("item_id") val itemId: String,
    @SerialName("uid") val uid: String,
    @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
)

@Serializable
data class ListItem(
    @SerialName("item_id") val itemId: String,
    @SerialName("item_title") val itemTitle: String,
    @SerialName("media_type") val mediaType: MediaType,
    @SerialName("cover_image") val coverImage: String,
    @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
) {
    companion object {
        fun ItemDetail.asListItem() = ListItem(
            itemId = itemId,
            itemTitle = title,
            mediaType = mediaType,
            coverImage = coverImage.orEmpty()
        )
    }
}

@Serializable
data class Bookshelf(
    @SerialName("bookshelf_id") val bookshelfId: String,
    @SerialName("name") val name: String,
    @SerialName("type") val type: Type,
    @SerialName("visibility") val visibility: Visibility,
    @SerialName("created_at") val createdAt: Instant = Clock.System.now(),
) {
    enum class Type { Favorite, Reading, Completed, Wishlist, Custom }
    enum class Visibility { Public, Private }

}

object BookshelfDefaults {
    val favorite = Bookshelf("favorite", "Favorites", Type.Favorite, Visibility.Public)
    val wishlist = Bookshelf("wishlist", "Wishlist", Type.Wishlist, Visibility.Public)
    val reading = Bookshelf("reading", "Wishlist", Type.Wishlist, Visibility.Public)
    val completed = Bookshelf("completed", "Completed", Type.Completed, Visibility.Public)

    val all = listOf(favorite, wishlist, reading, completed)
}
