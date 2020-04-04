package com.kafka.user.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.booksByAuthor
import com.kafka.domain.detail.ObserveItemDetail
import com.kafka.domain.detail.UpdateItemDetail
import com.kafka.domain.item.ObserveItems
import com.kafka.domain.item.UpdateItems
import com.kafka.domain.launchObserve
import com.kafka.ui.content.ContentDetailAction
import com.kafka.ui.content.ContentDetailViewState
import com.kafka.user.common.BaseViewModel
import com.kafka.user.util.Event
import com.kafka.user.util.ObservableLoadingCounter
import com.kafka.user.util.collectFrom
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for item detail.
 */
class ItemDetailViewModel @Inject constructor(
    private val updateItemDetail: UpdateItemDetail,
    private val observeItemDetail: ObserveItemDetail,
    observeItems: ObserveItems,
    private val updateItems: UpdateItems,
    private val loadingState: ObservableLoadingCounter
) : BaseViewModel<ContentDetailViewState>(ContentDetailViewState()) {
    private val query = ArchiveQuery().booksByAuthor("Kafka")

    private val pendingActions = Channel<ContentDetailAction>(Channel.BUFFERED)

    private val _navigateToContentDetailAction = MutableLiveData<Event<String>>()
    val navigateToContentDetailAction: LiveData<Event<String>>
        get() = _navigateToContentDetailAction

    init {
        viewModelScope.launch {
            loadingState.observable.collect { setState { copy(isLoading = it) } }
        }

        viewModelScope.launchObserve(observeItemDetail) {
            it.distinctUntilChanged().execute {
                updateItems(UpdateItems.Params(query))
                copy(itemDetail = it)
            }
        }

        viewModelScope.launchObserve(observeItems) {
            it.distinctUntilChanged().execute {
                copy(itemsByCreator = it)
            }
        }

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is ContentDetailAction.ContentItemClick -> _navigateToContentDetailAction.postValue(
                    Event(action.contentId)
                )
            }
        }

        observeItems(ObserveItems.Params(query))
    }

    fun observeItemDetail(contentId: String) {
        observeItemDetail(ObserveItemDetail.Param(contentId))
    }

    fun updateItemDetail(contentId: String) {
        updateItemDetail(UpdateItemDetail.Param(contentId)).also {
            viewModelScope.launch {
                loadingState.collectFrom(it)
            }
        }
    }

    fun submitAction(action: ContentDetailAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }
}
