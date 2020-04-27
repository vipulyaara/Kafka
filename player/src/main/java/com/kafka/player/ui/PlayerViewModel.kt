package com.kafka.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.kafka.data.query.ArchiveQuery
import com.kafka.domain.detail.ObserveItemDetail
import com.kafka.domain.item.ObserveBatchItems
import com.kafka.domain.item.UpdateBatchItems
import com.kafka.language.domain.ObserveSelectedLanguages
import com.kafka.player.domain.OperatePlayer
import com.kafka.ui.player.Play
import com.kafka.ui.player.PlayerAction
import com.kafka.ui.player.PlayerViewState
import com.kafka.ui.search.ContentItemClick
import com.kafka.ui.search.SearchAction
import com.kafka.ui.search.SearchViewState
import com.kafka.ui_common.BaseViewModel
import com.kafka.ui_common.Event
import com.kafka.ui_common.ObservableLoadingCounter
import com.kafka.ui_common.collectFrom
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

var playingItemId = ""

class PlayerViewModel @Inject constructor(
    private val observeBatchItems: ObserveBatchItems,
    private val loadingState: ObservableLoadingCounter,
    private val operatePlayer: OperatePlayer,
    observeItemDetail: ObserveItemDetail,
    private val updateBatchItems: UpdateBatchItems
) : BaseViewModel<PlayerViewState>(PlayerViewState()) {

    private val pendingActions = Channel<PlayerAction>(Channel.BUFFERED)

    init {
        viewModelScope.launch {
            loadingState.observable
                .distinctUntilChanged()
                .collect {
                    setState { copy(isLoading = it) }
                }
        }

        viewModelScope.launchObserve(observeItemDetail) {
            it.execute {
                copy(itemDetail = it)
            }
        }

        observeItemDetail(ObserveItemDetail.Param(playingItemId))

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is Play -> operatePlayer(OperatePlayer.Command.Play(action.contentId))
            }
        }
    }

    fun submitAction(action: PlayerAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    fun observeItems(queries: List<ArchiveQuery>) {
        observeBatchItems(ObserveBatchItems.Params(queries))
    }

    fun updateItems(queries: List<ArchiveQuery>) {
        updateBatchItems(UpdateBatchItems.Params(queries))
            .also { viewModelScope.launch { loadingState.collectFrom(it) } }
    }
}

