package com.kafka.content.ui.main

import com.kafka.data.entities.Language
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class MainViewState(
    val isLoading: Boolean = false,
    var selectedLanguages: List<Language>? = null
) : BaseViewState
