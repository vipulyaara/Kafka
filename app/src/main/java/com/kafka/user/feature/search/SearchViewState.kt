package com.kafka.user.feature.search

import com.airbnb.mvrx.MvRxState
import com.kafka.data.entities.Content

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class SearchViewState(
    val contents: List<Content>? = null,
    val isLoading: Boolean = false
) : MvRxState
