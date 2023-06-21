package com.kafka.data.model.homepage

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class HomepageCollectionResponse {
    abstract val enabled: Boolean
    abstract val topics: String
    abstract val index: Int

    @Serializable
    @SerialName("banners")
    data class Banners(
        val items: List<HomepageBanner> = listOf(),
        override val index: Int,
        override val enabled: Boolean = true,
        override val topics: String = ""
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("featuredItem")
    data class FeaturedItem(
        val label: String? = null,
        val itemIds: String,
        val image: List<FirebaseImage>? = null,
        override val index: Int,
        override val enabled: Boolean = true,
        override val topics: String = ""
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("recentItems")
    data class RecentItems(
        override val index: Int,
        override val enabled: Boolean = true,
        override val topics: String = ""
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("row")
    data class Row(
        val label: String,
        val itemIds: String,
        val clickable: Boolean = true,
        override val enabled: Boolean = true,
        override val index: Int = 0,
        override val topics: String = ""
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("column")
    data class Column(
        val label: String,
        val itemIds: String,
        val clickable: Boolean = true,
        override val enabled: Boolean = true,
        override val index: Int = 0,
        override val topics: String = ""
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("grid")
    data class Grid(
        val label: String,
        val itemIds: String,
        val clickable: Boolean = true,
        override val enabled: Boolean = true,
        override val index: Int = 0,
        override val topics: String = ""
    ) : HomepageCollectionResponse()
}

@Serializable
data class HomepageBanner(
    @DocumentId
    val id: String = "",
    val action: Action = Action.Search,
    val imageUrl: String = "",
    val keyword: String? = null,
    val index: Int = 0
) {
    @Serializable
    enum class Action {
        @SerialName("search")
        Search,

        @SerialName("item_detail")
        ItemDetail
    }
}

@Serializable
data class FirebaseImage(
    @DocumentId
    val ref: String,
    val downloadURL: String,
    val name: String = ""
)
