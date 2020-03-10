package com.kafka.user.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafka.data.content.ObserveContent
import com.kafka.data.data.interactor.launchObserve
import com.kafka.data.content.UpdateContent
import com.kafka.data.detail.ObserveContentDetail
import com.kafka.data.detail.UpdateContentDetail
import com.kafka.data.model.Event
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.booksByAuthor
import com.kafka.ui.content.ContentDetailAction
import com.kafka.ui.content.ContentDetailViewState
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
 * Implementation of [BaseViewModel] to provide data for content detail.
 */

var contentId = ""

class ContentDetailViewModel @Inject constructor(
    private val updateContentDetail: UpdateContentDetail,
    private val observeContentDetail: ObserveContentDetail,
    private val observeContent: ObserveContent,
    private val updateContent: UpdateContent,
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

        viewModelScope.launchObserve(observeContentDetail) {
            it.distinctUntilChanged().execute {
                updateContent(UpdateContent.Params(query))
                copy(itemDetail = it())
            }
        }

        viewModelScope.launchObserve(observeContent) {
            it.distinctUntilChanged().execute {
                copy(itemsByCreator = it.data)
            }
        }

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is ContentDetailAction.ContentItemClick -> _navigateToContentDetailAction.postValue(
                    Event(action.contentId)
                )
            }
        }

        observeContent(ObserveContent.Params(query))
    }

    fun observeContentDetail(contentId: String) {
        observeContentDetail(ObserveContentDetail.Param(contentId))
    }

    fun updateContentDetail(contentId: String) {
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
