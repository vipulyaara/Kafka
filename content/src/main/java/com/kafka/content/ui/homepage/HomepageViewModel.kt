package com.kafka.content.ui.homepage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.content.domain.followed.ObserveFollowedItems
import com.kafka.ui_common.base.ReduxViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomepageViewModel @ViewModelInject constructor(
    observeFollowedItems: ObserveFollowedItems
) : ReduxViewModel<HomepageViewState>(HomepageViewState()) {
    private val actioner = Channel<HomepageAction>(Channel.BUFFERED)

    init {
        viewModelScope.launchObserve(observeFollowedItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(favorites = it) }
        }

        viewModelScope.launch {
            actioner.consumeAsFlow().collect {
                when (it) {
                    is HomepageAction.SelectTag -> select(it.tag)
                }
            }
        }

        observeFollowedItems(Unit)
    }

    fun getSelectedAction() = currentState().tags.first { it.isSelected }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch {
            if (!actioner.isClosedForSend) { actioner.send(action) }
        }
    }

    private fun select(homepageTag: HomepageTag) {
        select(homepageTag.title)
    }

    private fun select(label: String) {
        currentState().apply {
            tags = tags.apply {
                forEach { it.isSelected = false }
                first { it.title == label }.isSelected = true
            }
        }
    }
}
