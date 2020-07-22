package com.kafka.ui.main

import com.kafka.data.entities.Language
import com.kafka.ui.actions.PlayerCommand
import com.kafka.ui.player.PlayerData
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class MainViewState(
    val isLoading: Boolean = false,
    var selectedLanguages: List<Language>? = null,
    var playerData: PlayerData = PlayerData(),
    val playerCommand: (PlayerCommand) -> Unit = {}
) : BaseViewState
