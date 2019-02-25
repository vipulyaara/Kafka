package com.airtel.kafkapp.feature.home

import com.airbnb.mvrx.MvRxState
import com.airtel.data.entities.Item
import com.airtel.data.model.ItemRail

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class HomepageViewState(
    val items: List<ItemRail>? = arrayListOf(),
    val isLoading: Boolean = false
) : MvRxState
