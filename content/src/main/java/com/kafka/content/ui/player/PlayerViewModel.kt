package com.kafka.content.ui.player

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.extensions.debug
import com.data.base.launchObserve
import com.kafka.content.domain.detail.ObserveItemDetail
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.ObservableLoadingCounter
import com.kafka.player.domain.*
import com.kafka.player.timber.extensions.isPlaying
import com.kafka.player.timber.playback.players.SongPlayer
import com.kafka.ui_common.base.ReduxViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class PlayerViewModel @ViewModelInject constructor(
    private val loadingState: ObservableLoadingCounter,
    private val addPlaylistItems: AddPlaylistItems,
    private val commandPlayer: CommandPlayer,
    observePlayer: ObservePlayer,
    songPlayer: SongPlayer,
    private val observeItemDetail: ObserveItemDetail
) : ReduxViewModel<PlayerViewState>(PlayerViewState()) {
    val pendingActions = Channel<PlayerAction>(Channel.BUFFERED)

    init {
        viewModelScope.launch {
            loadingState.observable.distinctUntilChanged().collectAndSetState { copy(isLoading = it) }
        }

        viewModelScope.launchObserve(observeItemDetail) {
            it.collectAndSetState { itemDetail ->
                itemDetail?.let { it -> updatePlaylistOnInit(it) }
                copy(itemDetail = itemDetail)
            }
        }
        viewModelScope.launchObserve(observePlayer) { it.collectAndSetState { copy(playerData = it) } }

        viewModelScope.launch {
            songPlayer.addStateChangeListener {
                viewModelScope.launchSetState { copy(playerData = playerData?.copy(isPlaying = it.isPlaying)) }
            }
        }

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is PlayerAction.Command -> commandPlayer(action)
            }
        }

        observePlayer(Unit)
    }

    private fun updatePlaylistOnInit(itemDetail: ItemDetail) {
        viewModelScope.launch {
            addPlaylistItems(AddPlaylistItems.Param(itemDetail.itemId)).collect {
                it.throwIfError()
                debug { "$it" }
            }
        }
    }

    fun play(itemDetail: ItemDetail?) {
        itemDetail?.apply {
            submitAction(PlayerAction.Command(PlayerCommand.Play(itemId)))
        }
    }

    fun observeItemDetail(it: String) {
        observeItemDetail(ObserveItemDetail.Param(it))
    }

    fun submitAction(action: PlayerAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    private fun commandPlayer(action: PlayerAction.Command) {
        when (val command = action.playerCommand) {
            is PlayerCommand.Play -> commandPlayer(PlayerCommand.Play(command.itemId, command.mediaId))
            is PlayerCommand.ToggleCurrent -> commandPlayer(PlayerCommand.ToggleCurrent)
        }
    }
}

