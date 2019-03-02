package com.airtel.kafkapp.feature.home

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airtel.data.data.annotations.UseInjection
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.feature.launchInteractor
import com.airtel.data.feature.query.QueryItems
import com.airtel.data.feature.query.QueryRepository
import com.airtel.data.model.RailItem
import com.airtel.data.util.AppCoroutineDispatchers
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
    private val dispatchers: AppCoroutineDispatchers by kodeinInstance.instance()
    private val loadingState = RxLoadingCounter()

    init {
        loadingState.observable
            .execute { copy(isLoading = it() ?: false) }

        QueryItems(dispatchers)
            .also { it.launchQuery(QueryItems.Params.ByCreator("Sir Arthur Conan Doyle")) }
            .observeQuery()

        QueryItems(dispatchers)
            .also { it.launchQuery(QueryItems.Params.ByCollection("librivoxaudio")) }
            .observeQuery()

        QueryItems(dispatchers)
            .also { it.launchQuery(QueryItems.Params.ByCreator("Franz Kafka")) }
            .observeQuery()
    }

    private fun QueryItems.observeQuery() {
        observe().toObservable()
            .subscribeOn(schedulers.io)
            .doOnError(logger::e)
            .execute {
                val list =
                    items?.toMutableSet().also { it?.add(RailItem(query.title ?: "", it())) }
                logger.d("Items ${it() ?: 0}")
                copy(items = list)
            }
    }

    private fun QueryItems.launchQuery(params: QueryItems.Params) {
        setParams(params)
        scope.launchInteractor(this, QueryItems.ExecuteParams())
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
