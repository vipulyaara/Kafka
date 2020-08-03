package com.kafka.content.ui.library

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.content.domain.followed.ObserveFollowedItems
import com.kafka.content.ui.homepage.HomepageAction
import com.kafka.ui_common.base.ReduxViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class LibraryViewModel @ViewModelInject constructor(
    observeFollowedItems: ObserveFollowedItems
) : ReduxViewModel<LibraryViewState>(LibraryViewState()) {
    private val actioner = Channel<HomepageAction>(Channel.BUFFERED)

    init {
        viewModelScope.launchObserve(observeFollowedItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(favorites = it) }
        }

        viewModelScope.launch {
            actioner.consumeAsFlow().collect {
                when (it) { }
            }
        }

        observeFollowedItems(Unit)
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch {
            if (!actioner.isClosedForSend) { actioner.send(action) }
        }
    }
}
