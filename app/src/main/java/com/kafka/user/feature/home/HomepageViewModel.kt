package com.kafka.user.feature.home

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.kafka.data.data.interactor.launchInteractor
import com.kafka.data.entities.Content
import com.kafka.data.model.RailItem
import com.kafka.user.extensions.logger
import com.kafka.user.feature.common.BaseMvRxViewModel
import com.kafka.user.ui.ObservableLoadingCounter
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Observable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import org.rekhta.data.util.data.ObservableLoadingCounter

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseMvRxViewModel] to provide data for homepage.
 */
class HomepageViewModel @AssistedInject constructor(
    @Assisted initialState: HomepageViewState,
    private val updateHomepage: UpdateHomepage,
    private val loadingState: ObservableLoadingCounter,
    ) : BaseMvRxViewModel<HomepageViewState>(HomepageViewState()) {

    init {
        viewModelScope.launch {
            loadingState.observable
                .distinctUntilChanged()
                .debounce(1200)
                .collect {
                    setState { copy(isLoading = it) }
                }
        }

        QueryItems(dispatchers)
            .also { it.launchQuery(QueryItems.Params.ByCreator("Franz Kafka")) }
            .observeQuery()
//            .zipWith(
//                QueryItems(dispatchers)
//                    .also { it.launchQuery(QueryItems.Params.ByCollection("librivoxaudio")) }
//                    .observeQuery()
//            )
//            .zipWith(
//                QueryItems(dispatchers)
//                    .also { it.launchQuery(QueryItems.Params.ByCollection("librivoxaudio")) }
//                    .observeQuery()
//            )
            .doOnError(logger::e)
            .execute { onItemsFetched(it) }
    }

    private fun HomepageViewState.onItemsFetched(it: Async<List<Content>>): HomepageViewState {
        val list =
            items?.toMutableSet().also { it?.add(RailItem(it.size.toString() + " Books by Kafka", it())) }
        logger.d("Items ${it() ?: 0}")
        return copy(items = list)
    }

    private fun QueryItems.observeQuery(): Observable<List<Content>> {
        return observe().toObservable()
            .subscribeOn(schedulers.io)
    }

    private fun QueryItems.launchQuery(params: QueryItems.Params) {
        setParams(params)
        scope.launchInteractor(this, QueryItems.ExecuteParams())
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: HomepageViewState): HomepageViewModel
    }

    companion object : MvRxViewModelFactory<HomepageViewModel, HomepageViewState> {
        override fun create(viewModelContext: ViewModelContext, state: HomepageViewState): HomepageViewModel? {
            val fragment: HomepageFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.discoverViewModelFactory.create(state)
        }
    }
}
