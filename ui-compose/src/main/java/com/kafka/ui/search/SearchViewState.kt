package com.kafka.ui.search

import com.kafka.data.entities.Item
import com.kafka.data.entities.Language
import com.kafka.data.item.RowItems
import com.kafka.ui.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class SearchViewState(
    val items: RowItems = RowItems(),
    val selectedLanguages: List<Language>? = null,
    val isLoading: Boolean = false
) : BaseViewState
