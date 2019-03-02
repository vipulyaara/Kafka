package com.airtel.kafkapp.feature.detail

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airtel.data.data.annotations.UseInjection
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.feature.detail.GetItemDetail
import com.airtel.data.feature.launchInteractor
import com.airtel.data.feature.query.QueryItems
import com.airtel.data.model.RailItem
import com.airtel.kafkapp.feature.common.BaseViewModel
import com.airtel.kafkapp.feature.home.detailId
import com.airtel.kafkapp.ui.RxLoadingCounter
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
