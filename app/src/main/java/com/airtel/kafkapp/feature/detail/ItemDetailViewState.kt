package com.airtel.kafkapp.feature.detail

import com.airtel.data.entities.Book
import com.airtel.data.entities.Item
import com.airtel.data.entities.ItemDetail
import com.airtel.kafkapp.feature.common.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ItemDetailViewState(
    var itemDetail: ItemDetail = ItemDetail(),
    var itemsByCreator: List<Item> = arrayListOf(),
    var isLoading: Boolean = false
) : BaseViewState()
