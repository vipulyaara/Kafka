package com.kafka.data.entities

import org.kafka.base.debug

data class Homepage(
    val recentItems: List<RecentItemWithProgress>,
    val homepageRows: List<CollectionItem>
) {
    sealed class CollectionItem

    data class Label(val text: String): CollectionItem()
    data class Row(val items: List<ItemPair>, val name: String): CollectionItem()
    data class Column(val items: List<Item>): CollectionItem()

    data class ItemPair(val first: Item, val second: Item?, val third: Item?) {
        fun any(condition: (Item?) -> Boolean) = listOf(first, second, third).any(condition)

        fun all() = listOf(first, second, third)
    }

    val continueReadingItems: List<RecentItemWithProgress>
        get() = recentItems.subList(
            fromIndex = 0,
            toIndex = ContinueReadingItemsThreshold.coerceAtMost(recentItems.size)
        )

    val hasSearchPrompt: Boolean
        get() {
            debug { "hasSearchPrompt: ${homepageRows}" }
            return homepageRows.isNotEmpty()
        }

    companion object {
        val Empty = Homepage(emptyList(), emptyList())
    }
}

inline fun Iterable<Homepage.ItemPair>.none(predicate: (Homepage.ItemPair) -> Boolean): Boolean {
    if (this is Collection && isEmpty()) return true
    for (element in this) if (predicate(element)) return false
    return true
}

private const val ContinueReadingItemsThreshold = 10
