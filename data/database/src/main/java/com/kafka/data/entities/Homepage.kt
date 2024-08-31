package com.kafka.data.entities

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class Homepage(val collection: List<HomepageCollection>) {
    val continueReadingItems: List<RecentItemWithProgress>
        get() = collection.recentItems.subList(
            fromIndex = 0,
            toIndex = ContinueReadingItemsThreshold.coerceAtMost(collection.recentItems.size),
        )

    val hasSearchPrompt: Boolean
        get() = collection.isNotEmpty()

    companion object {
        val Empty = Homepage(persistentListOf())
    }
}

val List<HomepageCollection>.recentItems
    get() = filterIsInstance<HomepageCollection.RecentItems>()
        .firstOrNull()?.items.orEmpty()

@Immutable
sealed class HomepageCollection {
    abstract val enabled: Boolean

    @Immutable
    data class FeaturedItem(
        val label: String?,
        val items: List<Item>,
        val image: List<String> = persistentListOf(),
        val shuffle: Boolean,
        override val enabled: Boolean = true,
    ) : HomepageCollection()

    @Immutable
    data class RecentItems(
        val items: List<RecentItemWithProgress>,
        override val enabled: Boolean = true,
    ) : HomepageCollection()

    @Immutable
    data class PersonRow(
        val items: List<String>,
        val images: List<String>,
        val clickable: Boolean = true,
        val shuffle: Boolean,
        override val enabled: Boolean = true,
    ) : HomepageCollection()

    @Immutable
    data class Row(
        val labels: List<String>,
        val items: List<Item>,
        val clickable: Boolean = true,
        val shuffle: Boolean,
        override val enabled: Boolean = true,
    ) : HomepageCollection() {
        val key = labels.joinToString(separator = ",")
    }

    @Immutable
    data class Column(
        val labels: List<String>,
        val items: List<Item>,
        val clickable: Boolean = true,
        val shuffle: Boolean,
        override val enabled: Boolean = true,
    ) : HomepageCollection() {
        val key = labels.joinToString(separator = ",")
    }

    @Immutable
    data class Grid(
        val labels: List<String>,
        val items: List<Item>,
        val clickable: Boolean = true,
        val shuffle: Boolean,
        override val enabled: Boolean = true,
    ) : HomepageCollection() {
        val key = labels.joinToString(separator = ",")
    }

    @Immutable
    data class Subjects(
        val items: List<String>,
        val clickable: Boolean = true,
        val shuffle: Boolean,
        override val enabled: Boolean = true,
    ) : HomepageCollection() {
        val key = items.joinToString(separator = ",")
    }
}

private const val ContinueReadingItemsThreshold = 30
