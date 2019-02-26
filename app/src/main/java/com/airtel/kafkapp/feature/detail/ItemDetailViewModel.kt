package com.airtel.kafkapp.feature.detail

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airtel.data.data.annotations.UseInjection
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.feature.detail.GetItemDetail
import com.airtel.data.feature.search.SearchItems
import com.airtel.data.feature.launchInteractor
import com.airtel.data.query.ArchiveQuery
import com.airtel.data.query.booksByAuthor
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
    private val searchItems: SearchItems by kodeinInstance.instance()
    private val loadingState = RxLoadingCounter()

    init {
        loadingState.observable
            .execute { copy(isLoading = it() ?: false) }

        getItemDetail.observe()
            .toObservable()
            .execute {
                searchItems.setParams(SearchItems.Params(ArchiveQuery().booksByAuthor(it()?.creator)))
                scope.launchInteractor(searchItems, SearchItems.ExecuteParams())

                copy(itemDetail = it())
            }

        searchItems.observe()
            .toObservable()
            .execute { copy(itemsByCreator = it()) }

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
