package com.kafka.content.ui.detail

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.extensions.debug
import com.data.base.launchObserve
import com.data.base.model.ArchiveQuery
import com.data.base.model.booksByAuthor
import com.kafka.content.domain.detail.ObserveItemDetail
import com.kafka.content.domain.detail.UpdateItemDetail
import com.kafka.content.domain.followed.ObserveItemFollowStatus
import com.kafka.content.domain.followed.UpdateFollowedItems
import com.kafka.content.domain.item.ObserveItems
import com.kafka.content.domain.item.UpdateItems
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.ObservableErrorCounter
import com.kafka.data.model.ObservableLoadingCounter
import com.kafka.data.model.collectFrom
import com.kafka.data.model.collectInto
import com.kafka.player.timber.playback.MediaSessionConnection
import com.kafka.ui_common.action.RealActioner
import com.kafka.ui_common.base.BaseViewModel
import com.kafka.ui_common.base.ReduxViewModel
import com.kafka.ui_common.extensions.showToast
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for item detail.
 */
class ItemDetailViewModel @ViewModelInject constructor(
    private val updateItemDetail: UpdateItemDetail,
    private val observeItemDetail: ObserveItemDetail,
    private val observeItems: ObserveItems,
    private val updateItems: UpdateItems,
    private val mediaSessionConnection: MediaSessionConnection,
    private val observeItemFollowStatus: ObserveItemFollowStatus,
    private val updateFollowedItems: UpdateFollowedItems,
    private val loadingState: ObservableLoadingCounter,
    private val errorState: ObservableErrorCounter
) : ReduxViewModel<ItemDetailViewState>(ItemDetailViewState()) {
    private val pendingActions = RealActioner<ItemDetailAction>()

    init {
        viewModelScope.launchObserve(observeItemDetail) { flow ->
            flow.distinctUntilChanged().collectAndSetState {
                debug { "item detail fetched $it" }

                observeByAuthor(it)
                copy(itemDetail = it)
            }
        }

        viewModelScope.launch {
            loadingState.observable.distinctUntilChanged().debounce(500).collectAndSetState { copy(isLoading = it) }
        }

        viewModelScope.launch {
            errorState.observable.distinctUntilChanged().collectAndSetState { copy(error = it?.message) }
        }

        viewModelScope.launchObserve(observeItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { list ->
                copy(itemsByCreator = list.filterNot { it.itemId == itemDetail?.itemId })
            }
        }

        viewModelScope.launchObserve(observeItemFollowStatus) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(isFavorite = it) }
        }

        viewModelScope.launch {
            pendingActions.observe {
                when (it) {
                    is ItemDetailAction.FavoriteClick -> {
                        updateFollowedItems(UpdateFollowedItems.Params(currentState().itemDetail!!.itemId))
                    }
                    else -> { }
                }
            }
        }
    }

    fun showFavoriteToast(context: Context) {
        val message = if (!currentState().isFavorite) "Added to favorites" else "Removed from favorites"
        context.showToast(message)
    }

    private fun observeByAuthor(itemDetail: ItemDetail?) {
        itemDetail?.let {
            debug { "observe query for creator ${itemDetail.creator}" }

            observeItemFollowStatus(ObserveItemFollowStatus.Params(itemDetail.itemId))
            ArchiveQuery("").booksByAuthor(itemDetail.creator).let {
                observeItems(ObserveItems.Params(it))
                updateItems(UpdateItems.Params(it))
            }
        }
    }

    fun observeItemDetail(contentId: String) {
        debug { "observe item detail for $contentId" }
        observeItemDetail(ObserveItemDetail.Param(contentId))
    }

    fun updateItemDetail(contentId: String) {
        debug { "update item detail for $contentId" }
        updateItemDetail(UpdateItemDetail.Param(contentId))
            .also { viewModelScope.launch { it.collectInto(loadingState) } }
            .also { viewModelScope.launch { errorState.collectFrom(it) } }
    }

    fun sendAction(action: ItemDetailAction) {
        viewModelScope.launch { pendingActions.sendAction(action) }
    }
}
