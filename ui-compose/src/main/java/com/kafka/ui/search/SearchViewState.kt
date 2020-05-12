package com.kafka.ui.search

import androidx.compose.Model
import androidx.ui.core.Modifier
import com.kafka.data.entities.Item
import com.kafka.data.entities.Language
import com.kafka.data.item.RowItems
import com.kafka.ui.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
@Model
data class SearchViewState(
    val query: String? = null,
    val selectedLanguages: List<Language>? = null,
    val items: RowItems = RowItems(),
    val isLoading: Boolean = false
) : BaseViewState
