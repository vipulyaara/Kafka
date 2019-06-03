package com.kafka.user.feature.detail

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.kafka.data.data.annotations.UseInjection
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.feature.detail.GetItemDetail
import com.kafka.data.data.interactor.launchInteractor
import com.kafka.data.feature.query.QueryItems
import com.kafka.data.model.RailItem
import com.kafka.user.feature.common.BaseViewModel
import com.kafka.user.feature.home.detailId
import com.kafka.user.ui.RxLoadingCounter
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for content detail.
 */
@UseInjection
internal class ItemDetailViewModel(itemId: String) : BaseViewModel<ItemDetailViewState>(
    ItemDetailViewState(itemId)
) {
    private val getItemDetail: GetItemDetail by kodeinInstance.instance()
    private val queryItems: QueryItems by kodeinInstance.instance()
    private val loadingState = RxLoadingCounter()

    init {
        loadingState.observable
            .execute { copy(isLoading = it() ?: false) }

        getItemDetail.observe()
            .toObservable()
            .doOnError(logger::e)
            .execute {
                itemDetail?.creator?.let {
                    queryItems.setParams(QueryItems.Params.ByCreator(it))
                    scope.launchInteractor(queryItems, QueryItems.ExecuteParams())
                }

                copy(itemDetail = it())
            }

        queryItems.observe()
            .toObservable()
            .execute { copy(itemsByCreator = RailItem(queryItems.query.title ?: "", it())) }

        withState {
            getItemDetail.setParams(GetItemDetail.Params(detailId))
            refresh()
        }
    }

    private fun refresh() {
        loadingState.addLoader()
        scope.launchInteractor(getItemDetail, GetItemDetail.ExecuteParams())
            .invokeOnCompletion { loadingState.removeLoader() }
    }

    companion object : MvRxViewModelFactory<ItemDetailViewModel, ItemDetailViewState> {
        // This *must* be @JvmStatic for performance reasons.
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: ItemDetailViewState
        ): ItemDetailViewModel {
            val fragment: ItemDetailFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return ItemDetailViewModel(fragment.arguments?.getString(MvRx.KEY_ARG) ?: "")
        }
    }
}
