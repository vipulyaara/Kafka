package com.kafka.data.entities

data class Homepage(
    val queryItems: List<Item>,
    val recentItems: List<ItemWithRecentItem>,
    val followedItems: List<Item>
)
