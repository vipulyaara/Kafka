package com.kafka.data.entities

import androidx.compose.runtime.Immutable
import com.kafka.data.model.homepage.HomepageBanner
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class Homepage(val collection: ImmutableList<HomepageCollection>) {
    val continueReadingItems: ImmutableList<RecentItemWithProgress>
        get() = collection.recentItems.subList(
            fromIndex = 0,
            toIndex = ContinueReadingItemsThreshold.coerceAtMost(collection.recentItems.size)
        )

    val hasSearchPrompt: Boolean
        get() = collection.isNotEmpty()

    companion object {
        val Empty = Homepage(persistentListOf())
    }
}

val ImmutableList<HomepageCollection>.recentItems
    get() = filterIsInstance<HomepageCollection.RecentItems>()
        .firstOrNull()?.items.orEmpty().toPersistentList()

@Immutable
sealed class HomepageCollection {
    abstract val enabled: Boolean

    @Immutable
    data class Banners(
        val items: ImmutableList<HomepageBanner>,
        override val enabled: Boolean = true
    ) : HomepageCollection()

    @Immutable
    data class FeaturedItem(
        val label: String?,
        val items: ImmutableList<Item>,
        val image: String? = null,
        override val enabled: Boolean = true
    ) : HomepageCollection()

    @Immutable
    data class RecentItems(
        val items: ImmutableList<RecentItemWithProgress>,
        override val enabled: Boolean = true
    ) : HomepageCollection()

    @Immutable
    data class Row(
        val labels: ImmutableList<String>,
        val items: ImmutableList<Item>,
        val clickable: Boolean = true,
        override val enabled: Boolean = true
    ) : HomepageCollection() {
        val key = labels.joinToString(separator = ",")
    }

    @Immutable
    data class Column(
        val labels: ImmutableList<String>,
        val items: ImmutableList<Item>,
        val clickable: Boolean = true,
        override val enabled: Boolean = true
    ) : HomepageCollection() {
        val key = labels.joinToString(separator = ",")
    }

    @Immutable
    data class Grid(
        val labels: ImmutableList<String>,
        val items: ImmutableList<Item>,
        val clickable: Boolean = true,
        override val enabled: Boolean = true
    ) : HomepageCollection() {
        val key = labels.joinToString(separator = ",")
    }
}

private const val ContinueReadingItemsThreshold = 10
