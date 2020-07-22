package com.kafka.user.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.data.query.languages
import com.kafka.language.domain.ObserveSelectedLanguages
import com.kafka.language.domain.UpdateLanguages
import com.kafka.player.domain.CommandPlayer
import com.kafka.player.domain.ObservePlayer
import com.kafka.ui.main.MainViewState
import com.kafka.ui_common.base.ReduxViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

class MainViewModel @ViewModelInject constructor(
    observePlayer: ObservePlayer,
    commandPlayer: CommandPlayer,
    observeSelectedLanguages: ObserveSelectedLanguages,
    private val updateLanguages: UpdateLanguages
) : ReduxViewModel<MainViewState>(MainViewState()) {

    init {
        viewModelScope.launchObserve(observeSelectedLanguages) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(selectedLanguages = it) }
        }

        viewModelScope.launchObserve(observePlayer) {
            it.collectAndSetState { copy(playerData = it, playerCommand = { commandPlayer(it) }) }
        }

        observePlayer(Unit)
        observeSelectedLanguages(Unit)
    }


    fun updateLanguages() {
        updateLanguages(UpdateLanguages.Params(languages))
    }
}
