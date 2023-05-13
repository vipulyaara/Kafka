package com.kafka.data.entities

import androidx.compose.runtime.Immutable
import com.google.firebase.firestore.DocumentId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
data class Homepage(
    val banners: ImmutableList<HomepageBanner>,
    val recentItems: ImmutableList<RecentItemWithProgress>,
    val collection: ImmutableList<HomepageCollection>
) {
    val continueReadingItems: ImmutableList<RecentItemWithProgress>
        get() = recentItems.subList(
            fromIndex = 0,
            toIndex = ContinueReadingItemsThreshold.coerceAtMost(recentItems.size)
        )

    val hasRecentItems: Boolean
        get() = recentItems.isNotEmpty()

    val hasSearchPrompt: Boolean
        get() = collection.isNotEmpty()

    companion object {
        val Empty = Homepage(persistentListOf(), persistentListOf(), persistentListOf())
    }
}

@Immutable
sealed class HomepageCollection {
    abstract val label: String
    abstract val items: ImmutableList<Item>
    abstract val labelClickable: Boolean
    abstract val enabled: Boolean

    @Immutable
    data class Row(
        override val label: String,
        override val items: ImmutableList<Item>,
        override val labelClickable: Boolean = true,
        override val enabled: Boolean = true
    ) : HomepageCollection()

    @Immutable
    data class Column(
        override val label: String,
        override val items: ImmutableList<Item>,
        override val labelClickable: Boolean = true,
        override val enabled: Boolean = true
    ) : HomepageCollection()
}

@Immutable
@Serializable
data class HomepageBanner(
    @DocumentId
    val id: String,
    val action: Action = Action.Search,
    val imageUrl: String = "",
    val enabled: Boolean = false,
    val keyword: String? = null,
    val index: Int = 0
) {
    @Serializable
    enum class Action {
        @SerialName("search")  Search,
        @SerialName("item_detail")  ItemDetail
    }
}


private const val ContinueReadingItemsThreshold = 10
