package com.kafka.user.feature.genre

import com.airbnb.mvrx.MvRxState
import com.kafka.data.entities.Item

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class GenreViewState(
    val items: List<Item>? = arrayListOf(),
    val isLoading: Boolean = false
) : MvRxState
