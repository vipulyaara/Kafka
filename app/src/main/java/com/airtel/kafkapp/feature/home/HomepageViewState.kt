package com.airtel.kafkapp.feature.home

import com.airbnb.mvrx.MvRxState
import com.airtel.data.model.RailItem

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class HomepageViewState(
    val items: Set<RailItem>? = hashSetOf(),
    val isLoading: Boolean = false
) : MvRxState
