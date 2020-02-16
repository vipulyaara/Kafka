package com.kafka.user.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafka.data.data.interactor.launchObserve
import com.kafka.data.entities.Content
import com.kafka.data.feature.content.ObserveContent
import com.kafka.data.feature.content.UpdateContent
import com.kafka.data.feature.detail.ObserveContentDetail
import com.kafka.data.feature.detail.UpdateContentDetail
import com.kafka.data.model.Event
import com.kafka.data.model.RailItem
import com.kafka.ui.content.ContentDetailAction
import com.kafka.ui.content.ContentDetailViewState
import com.kafka.user.feature.common.BaseMvRxViewModel
import com.kafka.user.feature.common.BaseViewModel
import com.kafka.user.ui.ObservableLoadingCounter
import com.kafka.user.ui.collectFrom
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseMvRxViewModel] to provide data for content detail.
 */

var contentId = ""

class ContentDetailViewModel @Inject constructor(
    private val updateContentDetail: UpdateContentDetail,
    private val observeContentDetail: ObserveContentDetail,
    private val updateContent: UpdateContent,
    private val observeContent: ObserveContent,
    private val loadingState: ObservableLoadingCounter
) : BaseViewModel<ContentDetailViewState>(ContentDetailViewState()) {

    private val pendingActions = Channel<ContentDetailAction>(Channel.BUFFERED)

    private val _navigateToContentDetailAction = MutableLiveData<Event<String>>()
    val navigateToContentDetailAction: LiveData<Event<String>>
        get() = _navigateToContentDetailAction

    init {
        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is ContentDetailAction.ContentItemClick -> _navigateToContentDetailAction.postValue(Event(action.contentId))
            }
        }

        viewModelScope.launchObserve(observeContentDetail) {
            it.distinctUntilChanged().execute {
                updateContent(UpdateContent.Params.ByCreator("Kafka"))
                copy(contentDetail = it())
            }
        }

        viewModelScope.launchObserve(observeContent) {
            it.distinctUntilChanged().execute {
                copy(itemsByCreator = it().toRailItem())
            }
        }

        viewModelScope.launch {
            loadingState.observable.collect { setState { copy(isLoading = it) } }
        }

        observeContentDetail(ObserveContentDetail.Param(contentId))
        observeContent(ObserveContent.Params.ByCreator("Kafka"))

        updateContentDetail(UpdateContentDetail.Param(contentId)).also {
            viewModelScope.launch {
                loadingState.collectFrom(it)
            }
        }
    }

    fun submitAction(action: ContentDetailAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }
}

private fun List<Content>.toRailItem(): RailItem {
    return toMutableSet().run {
        RailItem(
            "$size Books by Kafka",
            this@toRailItem.map { it })
    }
}
