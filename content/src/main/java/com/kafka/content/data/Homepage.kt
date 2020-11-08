package com.kafka.content.data

import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemWithRecentItem

data class Homepage(
    val queryItems: List<Item>,
    val recentItems: List<ItemWithRecentItem>,
    val followedItems: List<Item>
)
