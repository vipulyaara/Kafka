package com.kafka.content.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.player.domain.CommandPlayer
import com.kafka.player.domain.ObservePlayer
import com.kafka.ui_common.base.ReduxViewModel

class MainViewModel @ViewModelInject constructor(
    observePlayer: ObservePlayer,
    commandPlayer: CommandPlayer
) : ReduxViewModel<MainViewState>(MainViewState()) {

    init {
        viewModelScope.launchObserve(observePlayer) {
            it.collectAndSetState { copy(playerData = it, playerCommand = { commandPlayer(it) }) }
        }

        observePlayer(Unit)
    }

}
