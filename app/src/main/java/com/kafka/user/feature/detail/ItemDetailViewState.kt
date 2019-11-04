package com.kafka.user.feature.detail

import com.kafka.data.entities.ContentDetail
import com.kafka.data.model.RailItem
import com.kafka.user.feature.common.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ItemDetailViewState(
    val contentId: String,
    val contentDetail: ContentDetail? = null,
    val itemsByCreator: RailItem? = null,
    val isLoading: Boolean = false
) : BaseViewState {
    constructor(args: ItemDetailFragment.Arguments) : this(args.id)
}
