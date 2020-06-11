package com.kafka.user.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.base.extensions.debug
import com.data.base.launchObserve
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.booksByAuthor
import com.kafka.domain.ObservableErrorCounter
import com.kafka.domain.ObservableLoadingCounter
import com.kafka.domain.collectFrom
import com.kafka.domain.detail.ObserveItemDetail
import com.kafka.domain.detail.UpdateItemDetail
import com.kafka.domain.item.ObserveItems
import com.kafka.domain.item.UpdateItems
import com.kafka.domain.recent.AddRecentItem
import com.kafka.domain.recent.ObserveRecentItems
import com.kafka.player.domain.CommandPlayer
import com.kafka.player.ui.playingItemId
import com.kafka.ui.detail.ItemDetailAction
import com.kafka.ui.detail.ItemDetailViewState
import com.kafka.ui_common.BaseViewModel
import com.kafka.ui_common.Event
import kotlinx.coroutines.channels.Channel
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
    private val addRecentItem: AddRecentItem,
    private val commandPlayer: CommandPlayer,
    observeRecentItems: ObserveRecentItems,
    private val loadingState: ObservableLoadingCounter,
    private val errorState: ObservableErrorCounter
) : BaseViewModel<ItemDetailViewState>(ItemDetailViewState()) {
    private val pendingActions = Channel<ItemDetailAction>(Channel.BUFFERED)
    val navigateToContentDetailAction = MutableLiveData<Event<Item>>()
    val navigateToPlayerAction = MutableLiveData<Event<String>>()
    val navigateToReaderAction = MutableLiveData<Event<String?>>()
    var imageResource: Int = 0

    init {
        viewModelScope.launchObserve(observeItemDetail) {
            it.distinctUntilChanged().execute {
                observeByAuthor(it)
                debug { "item detail fetched $it" }
                playingItemId = it?.itemId ?: ""
                copy(itemDetail = it?.copy(coverImageResource = imageResource))
            }
        }

        viewModelScope.launch {
            loadingState.observable.distinctUntilChanged()
                .debounce(500).execute { copy(isLoading = it) }
        }

        viewModelScope.launch {
            errorState.observable.distinctUntilChanged()
                .debounce(500).execute {
                    copy(error = it?.message)
                }
        }

        viewModelScope.launch {
            loadingState.observable.distinctUntilChanged()
                .debounce(500).execute { copy(isLoading = it) }
        }

        viewModelScope.launchObserve(observeItems) {
            it.distinctUntilChanged().execute {
                copy(itemsByCreator = it.filterNot { it.itemId == itemDetail?.itemId })
            }
        }

        viewModelScope.launchObserve(observeRecentItems) {
            it.distinctUntilChanged().execute {
                debug { "recent items fetched ${it.size}" }
                val recentItem = it.firstOrNull { it.itemId == itemDetail?.itemId }
                debug { "recent item fetched $recentItem" }
                copy(recentItem = recentItem)
            }
        }

        observeRecentItems(Unit)

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is ItemDetailAction.RelatedItemClick -> navigateToContentDetailAction.postValue(Event(action.item))
                is ItemDetailAction.Play -> {
                    onPlayClicked()
                    navigateToPlayerAction.postValue(Event(viewState.value?.itemDetail?.itemId!!))
                }
                is ItemDetailAction.Read -> {
                    navigateToReaderAction.postValue(Event(getReaderUrl()))
                }
            }
        }
    }

    private fun getReaderUrl() =
        viewState.value?.itemDetail?.files?.firstOrNull { it.readerUrl != null }?.readerUrl

    private fun onPlayClicked() {
        addRecentItem(AddRecentItem.Params(viewState.value?.itemDetail?.itemId!!))
        commandPlayer(CommandPlayer.Command.Play(viewState.value?.itemDetail?.itemId!!))
    }

    private fun observeByAuthor(itemDetail: ItemDetail?) {
        itemDetail?.let {
            debug { "observe query for creator ${itemDetail.creator}" }
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
            .also { viewModelScope.launch { loadingState.collectFrom(it) } }
            .also { viewModelScope.launch { errorState.collectFrom(it) } }
    }

    fun submitAction(action: ItemDetailAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }
}
