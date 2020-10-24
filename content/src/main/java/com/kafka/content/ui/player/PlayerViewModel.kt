package com.kafka.content.ui.player

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.kafka.data.extensions.debug
import com.kafka.data.model.launchObserve
import com.kafka.content.domain.detail.ObserveItemDetail
import com.kafka.content.domain.followed.ObserveItemFollowStatus
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.ObservableLoadingCounter
import com.kafka.player.domain.*
import com.kafka.ui_common.base.ReduxViewModel
import com.kafka.ui_common.extensions.showToast
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class PlayerViewModel @ViewModelInject constructor(
    private val loadingState: ObservableLoadingCounter,
    private val addPlaylistItems: AddPlaylistItems,
    private val commandPlayer: CommandPlayer,
    private val observeItemDetail: ObserveItemDetail,
    private val observeItemFollowStatus: ObserveItemFollowStatus,
    observeCurrentSong: ObserveCurrentSong,
    observeQueueSongs: ObserveQueueSongs
) : ReduxViewModel<PlayerViewState>(PlayerViewState()) {

    init {
        viewModelScope.launch {
            loadingState.observable.distinctUntilChanged().collectAndSetState { copy(isLoading = it) }
        }

        viewModelScope.launchObserve(observeItemDetail) {
            it.collectAndSetState { itemDetail ->
                itemDetail?.let { it ->
                    updatePlaylistOnInit(it)
                    observeItemFollowStatus(ObserveItemFollowStatus.Params(itemDetail.itemId))
                }
                copy(itemDetail = itemDetail)
            }
        }

        viewModelScope.launchObserve(observeCurrentSong) {
            it.collectAndSetState {
                debug { "Current playing song $it" }
                copy(currentSong = it)
            }
        }

        viewModelScope.launchObserve(observeQueueSongs) { it.collectAndSetState { copy(queueSongs = it) } }

        viewModelScope.launchObserve(observeItemFollowStatus) { it.collectAndSetState { copy(isFavorite = it) } }

        observeCurrentSong(Unit)
        observeQueueSongs(Unit)
    }

    private fun showFavoriteToast(context: Context) {
        val message = if (!currentState().isFavorite) "Added to favorites" else "Removed from favorites"
        context.showToast(message)
    }

    private fun updatePlaylistOnInit(itemDetail: ItemDetail) {
        viewModelScope.launch {
            addPlaylistItems(AddPlaylistItems.Param(itemDetail.itemId))
        }
    }

    fun observeItemDetail(it: String) {
        observeItemDetail(ObserveItemDetail.Param(it))
    }

    fun command(command: PlayerCommand) {
        when (command) {
            is PlayerCommand.Play -> commandPlayer(PlayerCommand.Play(command.mediaId))
            is PlayerCommand.ToggleCurrent -> commandPlayer(PlayerCommand.ToggleCurrent)
            is PlayerCommand.Previous -> commandPlayer(PlayerCommand.Previous)
            is PlayerCommand.Next -> commandPlayer(PlayerCommand.Next)
        }
    }
}
