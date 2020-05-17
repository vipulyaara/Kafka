package com.kafka.ui.home

import com.kafka.data.item.RowItems
import com.kafka.ui_common.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class HomepageViewState(
    var items: RowItems = RowItems(),
    var isLoading: Boolean = false
) : BaseViewState
