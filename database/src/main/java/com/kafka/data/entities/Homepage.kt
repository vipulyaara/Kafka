package com.kafka.data.entities

data class Homepage(
    val recentItems: List<RecentItemWithProgress>,
    val collection: List<HomepageCollection>
) {

    val continueReadingItems: List<RecentItemWithProgress>
        get() = recentItems.subList(
            fromIndex = 0,
            toIndex = ContinueReadingItemsThreshold.coerceAtMost(recentItems.size)
        )

    val hasSearchPrompt: Boolean
        get() {
            return collection.isNotEmpty()
        }

    companion object {
        val Empty = Homepage(emptyList(), emptyList())
    }
}

sealed class HomepageCollection {
    abstract val label: String
    abstract val items: List<Item>
    abstract val labelClickable: Boolean
    abstract val enabled: Boolean

    data class Row(
        override val label: String,
        override val items: List<Item>,
        override val labelClickable: Boolean = true,
        override val enabled: Boolean = true
    ) : HomepageCollection()

    data class Column(
        override val label: String,
        override val items: List<Item>,
        override val labelClickable: Boolean = true,
        override val enabled: Boolean = true
    ) : HomepageCollection()
}


private const val ContinueReadingItemsThreshold = 10
