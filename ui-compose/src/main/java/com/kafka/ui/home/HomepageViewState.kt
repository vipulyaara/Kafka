package com.kafka.ui.home

import androidx.compose.Model
import com.kafka.data.entities.Item
import com.kafka.ui_common.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
@Model
data class HomepageViewState(
    var items: List<Item>? = null,
    var isLoading: Boolean = false
) : BaseViewState
