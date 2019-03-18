package com.kafka.user.feature.detail

import com.airbnb.mvrx.MvRxState
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.RailItem

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ItemDetailViewState(
    val itemId: String,
    val itemDetail: ItemDetail? = null,
    val itemsByCreator: RailItem? = null,
    val isLoading: Boolean = false
) : MvRxState {
    constructor(args: ItemDetailFragment.Arguments) : this(args.id)
}
