package com.kafka.user.feature.home

import com.airbnb.mvrx.MvRxState
import com.kafka.data.model.RailItem

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class HomepageViewState(
    val items: Set<RailItem>? = hashSetOf(),
    val isLoading: Boolean = false
) : MvRxState
