package com.kafka.user.feature.player

import com.airbnb.mvrx.MvRxState
import com.kafka.data.model.RailItem

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class PlayerViewState(
    val items: Set<RailItem>? = hashSetOf(),
    val isLoading: Boolean = false
) : MvRxState
