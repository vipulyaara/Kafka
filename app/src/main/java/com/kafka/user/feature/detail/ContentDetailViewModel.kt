package com.kafka.user.feature.detail

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.*
import com.kafka.data.data.annotations.UseInjection
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.interactor.launchObserve
import com.kafka.data.feature.detail.ObserveContentDetail
import com.kafka.data.feature.detail.UpdateContentDetail
import com.kafka.user.feature.common.BaseMvRxViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.rekhta.data.util.data.ObservableLoadingCounter

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseMvRxViewModel] to provide data for content detail.
 */
internal class ContentDetailViewModel  @AssistedInject constructor(
    @Assisted initialState: ContentDetailViewState,
    private val updateContentDetail: UpdateContentDetail,
    private val observeContentDetail: ObserveContentDetail,
    private val loadingState: ObservableLoadingCounter
) : BaseMvRxViewModel<ContentDetailViewState>(initialState) {

    init {

        viewModelScope.launchObserve(observeContentDetail) {
            it.distinctUntilChanged().collect { result ->
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

        refresh()
    }

    private fun refresh() {
        loadingState.addLoader()
        scope.launchInteractor(observeContentDetail, ObserveContentDetail.ExecuteParams())
            .invokeOnCompletion { loadingState.removeLoader() }
    }
}
