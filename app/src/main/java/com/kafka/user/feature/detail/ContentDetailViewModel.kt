package com.kafka.user.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.*
import com.kafka.data.data.annotations.UseInjection
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.feature.detail.ObserveContentDetail
import com.kafka.data.data.interactor.launchObserve
import com.kafka.data.feature.detail.UpdateContentDetail
import com.kafka.data.feature.query.QueryItems
import com.kafka.data.model.RailItem
import com.kafka.user.feature.common.BaseViewModel
import com.kafka.user.feature.home.detailId
import com.kafka.user.ui.ObservableLoadingCounter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for content detail.
 */
@UseInjection
internal class ContentDetailViewModel(contentId: String) : BaseViewModel<ItemDetailViewState>(
    ItemDetailViewState(contentId)
) {
    private val observeContentDetail: ObserveContentDetail by kodeinInstance.instance()
    private val updateContentDetail: UpdateContentDetail by kodeinInstance.instance()
    private val queryItems: QueryItems by kodeinInstance.instance()
    private val loadingState = ObservableLoadingCounter()
    private val viewState: LiveData<ItemDetailViewState> = observeContentDetail.observe().asLiveData()

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
