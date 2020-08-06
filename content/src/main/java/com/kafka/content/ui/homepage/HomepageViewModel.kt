package com.kafka.content.ui.homepage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.content.domain.followed.ObserveFollowedItems
import com.kafka.content.domain.recent.ObserveRecentItems
import com.kafka.ui_common.base.ReduxViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomepageViewModel @ViewModelInject constructor(
    observeFollowedItems: ObserveFollowedItems,
    observeRecentItems: ObserveRecentItems
) : ReduxViewModel<HomepageViewState>(HomepageViewState()) {
    private val actioner = Channel<HomepageAction>(Channel.BUFFERED)

    init {
        viewModelScope.launchObserve(observeFollowedItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(favorites = it) }
        }

        viewModelScope.launchObserve(observeRecentItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(recentItems = it) }
        }

        viewModelScope.launch {
            actioner.consumeAsFlow().collect {
                when (it) {
                    is HomepageAction.SelectTag -> selectTag(it.tag)
                }
            }
        }

        observeFollowedItems(Unit)
        observeRecentItems(Unit)
    }

    fun getSelectedTag() = currentState().tags.first { it.isSelected }

    fun addTag(title: String) {
        if (currentState().tags.map { it.title }.contains(title)) return
        val tag = HomepageTag(title)
        viewModelScope.launch {
            setState { copy(tags = tags.toMutableList().also { it.add(0, tag) }) }
            submitAction(HomepageAction.SelectTag(tag))
        }
    }

    fun submitAction(action: HomepageAction) {
        viewModelScope.launch {
            if (!actioner.isClosedForSend) {
                actioner.send(action)
            }
        }
    }

    private fun selectTag(homepageTag: HomepageTag) {
        currentState().apply {
            tags = tags.apply {
                forEach { it.isSelected = false }
                first { it.title == homepageTag.title }.isSelected = true
            }
        }
    }
}
