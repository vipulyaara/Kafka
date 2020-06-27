package com.kafka.ui.detail

import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.RecentItem
import com.kafka.ui_common.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ItemDetailViewState(
    val recentItem: RecentItem? = null,
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null,
    val itemsByCreator: List<Item>? = null,
    val isLoading: Boolean = true,
    val error: String? = null
) : BaseViewState
