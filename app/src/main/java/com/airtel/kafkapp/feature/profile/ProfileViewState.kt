package com.airtel.kafkapp.feature.profile

import com.airbnb.mvrx.MvRxState
import com.airtel.data.entities.Item
import com.airtel.data.model.RailItem

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ProfileViewState(
    val items: List<Item>? = arrayListOf(),
    val isLoading: Boolean = false
) : MvRxState
