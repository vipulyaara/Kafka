package com.kafka.data.entities

data class Homepage(
    val queryItems: List<Item>,
    val recentItems: List<RecentItem>,
    val favoriteItems: List<FavoriteItem>
) {
    val continueReadingItems: List<RecentItem>
        get() = recentItems.subList(
            fromIndex = 0,
            toIndex = ContinueReadingItemsThreshold.coerceAtMost(recentItems.size)
        )
}

private const val ContinueReadingItemsThreshold = 10
