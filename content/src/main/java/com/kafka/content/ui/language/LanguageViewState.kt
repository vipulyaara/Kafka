package com.kafka.content.ui.language

import com.kafka.data.entities.Language
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class LanguageViewState(
    var languages: List<Language>? = null
) : BaseViewState
