package com.kafka.content.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.data.query.languages
import com.kafka.language.domain.UpdateLanguages
import com.kafka.player.domain.ObservePlayer
import com.kafka.ui.search.HomepageViewState
import com.kafka.ui_common.BaseViewModel

class MainViewModel @ViewModelInject constructor(
    private val updateLanguages: UpdateLanguages,
    observePlayer: ObservePlayer
) : BaseViewModel<HomepageViewState>(HomepageViewState()) {

    init {
        viewModelScope.launchObserve(observePlayer) {
            it.execute { copy(playerData = it) }
        }

        observePlayer(Unit)
    }

    fun updateLanguages() {
        updateLanguages(UpdateLanguages.Params(languages))
    }
}

