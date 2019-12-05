package com.kafka.user.feature.search

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.interactor.launchInteractor
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.searchByKeyword
import com.kafka.data.util.AppRxSchedulers
import com.kafka.user.ui.ObservableLoadingCounter
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for search.
 */
class SearchViewModel : BaseViewModel<SearchViewState>(
    SearchViewState()
) {
    private val schedulers: AppRxSchedulers by kodeinInstance.instance()
    private val searchItems: SearchItems by kodeinInstance.instance()
    private val loadingState = ObservableLoadingCounter()

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
