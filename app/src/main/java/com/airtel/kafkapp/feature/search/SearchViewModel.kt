package com.airtel.kafkapp.feature.search

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airtel.data.data.annotations.UseInjection
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.feature.item.SearchItems
import com.airtel.data.feature.launchInteractor
import com.airtel.data.query.ArchiveQuery
import com.airtel.data.query.searchByKeyword
import com.airtel.data.util.AppRxSchedulers
import com.airtel.kafkapp.feature.common.BaseViewModel
import com.airtel.kafkapp.ui.RxLoadingCounter
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for search.
 */
@UseInjection
class SearchViewModel : BaseViewModel<SearchViewState>(
    SearchViewState()
) {
    private val schedulers: AppRxSchedulers by kodeinInstance.instance()
    private val searchItems: SearchItems by kodeinInstance.instance()
    private val loadingState = RxLoadingCounter()

    init {
        loadingState.observable
            .execute { copy(isLoading = it() ?: false) }

        searchItems.observe()
            .toObservable()
            .subscribeOn(schedulers.io)
            .execute { copy(items = it()) }

        refresh("Kafka")
    }

    private fun refresh(keyword: String) {
        searchItems.setParams(SearchItems.Params(ArchiveQuery().searchByKeyword(keyword)))
        scope.launchInteractor(searchItems, SearchItems.ExecuteParams())
    }

    companion object : MvRxViewModelFactory<SearchViewModel, SearchViewState> {
        // This *must* be @JvmStatic for performance reasons.
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: SearchViewState
        ): SearchViewModel {
            return SearchViewModel()
        }
    }
}
