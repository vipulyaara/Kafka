package com.airtel.kafkapp.feature.home

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airtel.data.data.annotations.UseInjection
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.feature.search.SearchItems
import com.airtel.data.feature.launchInteractor
import com.airtel.data.model.ItemRail
import com.airtel.data.query.ArchiveQuery
import com.airtel.data.query.booksByAuthor
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
class HomepageViewModel : BaseViewModel<HomepageViewState>(
    HomepageViewState()
) {
    private val schedulers: AppRxSchedulers by kodeinInstance.instance()
    private val booksByAuthor: SearchItems by kodeinInstance.instance()
    private val loadingState = RxLoadingCounter()

    init {
        loadingState.observable
            .execute { copy(isLoading = it() ?: false) }

        booksByAuthor.observe()
            .toObservable()
            .subscribeOn(schedulers.io)
            .doOnError(logger::e)
            .execute {
                copy(items = arrayListOf(ItemRail("Suggested Books", it())))
            }

        refresh("Franz Kafka")
//        refresh("librivoxaudio")
    }

    private fun refresh(author: String) {
        booksByAuthor.setParams(SearchItems.Params(ArchiveQuery().booksByAuthor(author)))
        scope.launchInteractor(booksByAuthor, SearchItems.ExecuteParams())
    }

    companion object : MvRxViewModelFactory<HomepageViewModel, HomepageViewState> {
        // This *must* be @JvmStatic for performance reasons.
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: HomepageViewState
        ): HomepageViewModel {
            return HomepageViewModel()
        }
    }
}
