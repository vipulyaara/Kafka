package com.kafka.ui.home

import com.kafka.data.model.RailItem
import com.kafka.ui.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class HomepageViewState(
    val items: Set<RailItem>? = hashSetOf(),
    val isLoading: Boolean = false
) : BaseViewState
