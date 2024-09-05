package com.kafka.data.model.homepage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class HomepageCollectionResponse {
    abstract val enabled: Boolean
    abstract val topics: String
    abstract val index: Float

    @Serializable
    @SerialName("featuredItem")
    data class FeaturedItem(
        val label: String? = null,
        val itemIds: String,
        val image: List<FirebaseImage>? = null,
        val shuffle: Boolean = false,
        override val index: Float,
        override val enabled: Boolean = true,
        override val topics: String = "",
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("person")
    data class PersonRow(
        val itemIds: String,
        val image: List<FirebaseImage>,
        val clickable: Boolean = true,
        val shuffle: Boolean = false,
        override val enabled: Boolean = true,
        override val index: Float,
        override val topics: String = "",
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("recentItems")
    data class RecentItems(
        override val index: Float,
        override val enabled: Boolean = true,
        override val topics: String = "",
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("recommendation")
    data class Recommendation(
        val label: String,
        val itemIds: String,
        override val index: Float,
        override val enabled: Boolean = true,
        override val topics: String = "",
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("subject")
    data class Subjects(
        val itemIds: String,
        val clickable: Boolean = true,
        val shuffle: Boolean = false,
        override val index: Float,
        override val enabled: Boolean = true,
        override val topics: String = "",
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("row")
    data class Row(
        val label: String,
        val itemIds: String,
        val clickable: Boolean = true,
        val shuffle: Boolean = false,
        override val enabled: Boolean = true,
        override val index: Float = 0f,
        override val topics: String = "",
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("column")
    data class Column(
        val label: String,
        val itemIds: String,
        val clickable: Boolean = true,
        val shuffle: Boolean = false,
        override val enabled: Boolean = true,
        override val index: Float = 0f,
        override val topics: String = "",
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("grid")
    data class Grid(
        val label: String,
        val itemIds: String,
        val clickable: Boolean = true,
        val shuffle: Boolean = false,
        override val enabled: Boolean = true,
        override val index: Float = 0f,
        override val topics: String = "",
    ) : HomepageCollectionResponse()

    @Serializable
    data class Unknown(
        override val enabled: Boolean = false,
        override val topics: String = "",
        override val index: Float = -1f,
    ) : HomepageCollectionResponse()
}

@Serializable
data class FirebaseImage(
    @SerialName("id")
    val ref: String,
    val downloadURL: String,
    val name: String = "",
)
