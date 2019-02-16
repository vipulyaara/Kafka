package com.airtel.kafkapp.feature.home

import com.airbnb.mvrx.MvRxState
import com.airtel.data.entities.Item

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class HomepageViewState(
    val items: List<Item>? = null,
    val isLoading: Boolean = false
) : MvRxState
