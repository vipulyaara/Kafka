package com.kafka.ui.search

import androidx.compose.Model
import com.kafka.data.entities.Item
import com.kafka.data.entities.Language
import com.kafka.ui_common.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
@Model
data class SearchViewState(
    var query: String? = null,
    var selectedLanguages: List<Language>? = null,
    var items: List<Item>? = null,
    var isLoading: Boolean = false
) : BaseViewState
