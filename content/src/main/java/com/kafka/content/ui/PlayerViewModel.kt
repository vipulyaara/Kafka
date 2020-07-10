package com.kafka.content.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.content.domain.detail.ObserveItemDetail
import com.kafka.content.domain.item.ObserveBatchItems
import com.kafka.content.domain.item.UpdateBatchItems
import com.kafka.data.model.ObservableLoadingCounter
import com.kafka.data.model.collectFrom
import com.data.base.model.ArchiveQuery
import com.kafka.player.domain.CommandPlayer
import com.kafka.player.domain.ObservePlayer
import com.kafka.ui.actions.PlayerCommand
import com.kafka.ui.player.PlayerViewState
import com.kafka.ui_common.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

var playingItemId = ""

class PlayerViewModel @ViewModelInject constructor(
    private val observeBatchItems: ObserveBatchItems,
    private val loadingState: ObservableLoadingCounter,
    private val commandPlayer: CommandPlayer,
    observePlayer: ObservePlayer,
    observeItemDetail: ObserveItemDetail,
    private val updateBatchItems: UpdateBatchItems
) : BaseViewModel<PlayerViewState>(PlayerViewState()) {

    private val pendingActions = Channel<PlayerCommand>(Channel.BUFFERED)

    init {
        viewModelScope.launch {
            loadingState.observable
                .distinctUntilChanged()
                .collect {
                    setState { copy(isLoading = it) }
                }
        }

        viewModelScope.launchObserve(observeItemDetail) { it.execute { copy(itemDetail = it) } }
        viewModelScope.launchObserve(observePlayer) { it.execute { copy(playerData = it) } }

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is PlayerCommand.Play -> commandPlayer(PlayerCommand.Play(action.itemId))
                is PlayerCommand.ToggleCurrent -> commandPlayer(PlayerCommand.ToggleCurrent)
            }
        }

        observeItemDetail(ObserveItemDetail.Param(playingItemId))
        observePlayer(Unit)
    }

    fun submitAction(command: PlayerCommand) {
        viewModelScope.launch { pendingActions.send(command) }
    }

    fun observeItems(queries: List<ArchiveQuery>) {
        observeBatchItems(com.kafka.content.domain.item.ObserveBatchItems.Params(queries))
    }

    fun updateItems(queries: List<ArchiveQuery>) {
        updateBatchItems(com.kafka.content.domain.item.UpdateBatchItems.Params(queries))
            .also { viewModelScope.launch { loadingState.collectFrom(it) } }
    }
}

