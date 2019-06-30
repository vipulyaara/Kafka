package com.kafka.user.feature.language

import com.airbnb.mvrx.MvRxState
import com.kafka.data.model.LanguageModel

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class LanguageViewState(
    val languages: List<LanguageModel>? = null,
    val isLoading: Boolean = false
) : MvRxState {
}
