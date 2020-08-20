package com.kafka.content.ui.homepage

import com.kafka.content.R
import com.kafka.content.domain.homepage.HomepageTag
import com.kafka.content.ui.search.SearchViewState
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemWithRecentItem
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class HomepageViewState(
    val searchViewState: SearchViewState = SearchViewState(),
    var favorites: List<Item>? = null,
    var recentItems: List<ItemWithRecentItem>? = null,
    var tags: List<HomepageTag>? = null,
    val error: Throwable? = null
) : BaseViewState

val bannerImages = listOf(
    R.drawable.img_banner_26,
    R.drawable.img_banner_30,
    R.drawable.img_banner_31,
    R.drawable.img_banner_32,
    R.drawable.img_banner_33
)

