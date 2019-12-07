package com.kafka.user.feature.detail

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.kafka.data.data.interactor.launchObserve
import com.kafka.data.feature.detail.ObserveContentDetail
import com.kafka.data.feature.detail.UpdateContentDetail
import com.kafka.user.feature.common.BaseMvRxViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import com.kafka.user.ui.ObservableLoadingCounter
import com.kafka.user.ui.collectFrom

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseMvRxViewModel] to provide data for content detail.
 */
class ContentDetailViewModel  @AssistedInject constructor(
    @Assisted initialState: ContentDetailViewState,
    private val updateContentDetail: UpdateContentDetail,
    private val observeContentDetail: ObserveContentDetail,
    private val loadingState: ObservableLoadingCounter
) : BaseMvRxViewModel<ContentDetailViewState>(initialState) {

    init {
        viewModelScope.launchObserve(observeContentDetail) {
            it.distinctUntilChanged().execute { result ->
                if (result is Success) {
                    val value = result()
                    copy(contentDetail = value)
                } else {
                    this
                }
            }
        }

        viewModelScope.launch {
            loadingState.observable.collect { setState { copy(isLoading = it) } }
        }

        withState {
            observeContentDetail(ObserveContentDetail.Param(it.contentId))
        }

        withState {
            updateContentDetail(UpdateContentDetail.Param(it.contentId)).also {
                viewModelScope.launch {
                    loadingState.collectFrom(it)
                }
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: ContentDetailViewState): ContentDetailViewModel
    }

    companion object : MvRxViewModelFactory<ContentDetailViewModel, ContentDetailViewState> {
        override fun create(viewModelContext: ViewModelContext, state: ContentDetailViewState): ContentDetailViewModel? {
            val fragment: ContentDetailFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
