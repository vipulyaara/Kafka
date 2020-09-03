package com.kafka.content.ui.library

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.content.domain.followed.ObserveFollowedItems
import com.kafka.ui_common.base.ReduxViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged

class LibraryViewModel @ViewModelInject constructor(
    observeFollowedItems: ObserveFollowedItems
) : ReduxViewModel<LibraryViewState>(LibraryViewState()) {
    private val actioner = Channel<LibraryAction>(Channel.BUFFERED)

    init {
        viewModelScope.launchObserve(observeFollowedItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(favorites = it) }
        }

        observeFollowedItems(Unit)
    }
}
