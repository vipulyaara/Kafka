package com.kafka.ui.home

import com.kafka.data.entities.Item
import com.kafka.data.item.RowItems
import com.kafka.ui.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class HomepageViewState(
    val items: RowItems = RowItems(),
    val isLoading: Boolean = false
) : BaseViewState
