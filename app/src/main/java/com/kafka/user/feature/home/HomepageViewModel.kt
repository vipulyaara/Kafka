package com.kafka.user.feature.home

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.kafka.data.data.annotations.UseInjection
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.interactor.launchInteractor
import com.kafka.data.entities.Item
import com.kafka.data.feature.query.QueryItems
import com.kafka.data.model.RailItem
import com.kafka.data.util.AppCoroutineDispatchers
import com.kafka.data.util.AppRxSchedulers
import com.kafka.user.feature.common.BaseViewModel
import com.kafka.user.ui.RxLoadingCounter
import io.reactivex.Observable
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
        loadingState.observable.execute { copy(isLoading = it() ?: false) }

        QueryItems(dispatchers)
            .also { it.launchQuery(QueryItems.Params.ByCreator("Mark Twain")) }
            .observeQuery()
            .concatMap {
                QueryItems(dispatchers)
                    .also { it.launchQuery(QueryItems.Params.ByCollection("librivoxaudio")) }
                    .observeQuery()
            }
            .concatMap {
                QueryItems(dispatchers)
                    .also { it.launchQuery(QueryItems.Params.ByCreator("Franz Kafka")) }
                    .observeQuery()
            }
            .doOnError(logger::e)
            .execute { onItemsFetched(it) }
    }

    private fun HomepageViewState.onItemsFetched(it: Async<List<Item>>): HomepageViewState {
        val list =
            items?.toMutableSet().also { it?.add(RailItem(it.size.toString() + " Books by Kafka", it())) }
        logger.d("Items ${it() ?: 0}")
        return copy(items = list)
    }

    private fun QueryItems.observeQuery(): Observable<List<Item>> {
        return observe().toObservable()
            .subscribeOn(schedulers.io)
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
