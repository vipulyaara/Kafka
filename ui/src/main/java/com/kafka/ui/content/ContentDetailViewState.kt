package com.kafka.ui.content

import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.ui.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ContentDetailViewState(
    val contentId: String = "",
    val itemDetail: ItemDetail? = null,
    val itemsByCreator: List<Item>? = null,
    val isLoading: Boolean = true
) : BaseViewState
