package com.kafka.ui.player

import com.kafka.ui.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class PlayerViewState(

    val isLoading: Boolean = false
) : BaseViewState

data class PlayerData(
    val contentId: String,
    val title: String?

)
