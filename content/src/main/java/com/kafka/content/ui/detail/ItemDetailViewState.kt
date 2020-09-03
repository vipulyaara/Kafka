package com.kafka.content.ui.detail

import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ItemDetailViewState(
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null,
    val itemsByCreator: List<Item>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : BaseViewState
