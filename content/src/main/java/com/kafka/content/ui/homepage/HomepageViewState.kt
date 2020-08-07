package com.kafka.content.ui.homepage

import com.kafka.content.ui.search.SearchViewState
import com.kafka.data.entities.Item
import com.kafka.data.entities.RecentItem
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class HomepageViewState(
    val searchViewState: SearchViewState = SearchViewState(),
    var favorites: List<Item>? = null,
    var recentItems: List<RecentItem>? = null,
    var tags: List<HomepageTag> = suggestedTags
) : BaseViewState

val suggestedTags = listOf(
    HomepageTag("Mirza Ghalib", true),
    HomepageTag("Camus", false),
    HomepageTag("Meer Taqi", false),
    HomepageTag("Dostoyevsky", false),
    HomepageTag("Kafka", false)
)

data class HomepageTag(var title: String, var isSelected: Boolean = false)
