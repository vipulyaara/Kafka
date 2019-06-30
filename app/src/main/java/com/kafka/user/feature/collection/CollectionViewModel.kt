package com.kafka.user.feature.collection

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.kafka.data.data.annotations.UseInjection
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.interactor.launchInteractor
import com.kafka.data.feature.query.QueryItems
import com.kafka.data.util.AppRxSchedulers
import com.kafka.user.feature.common.BaseViewModel
import com.kafka.user.ui.RxLoadingCounter
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for search.
 */
@UseInjection
class CollectionViewModel : BaseViewModel<CollectionViewState>(
    CollectionViewState()
) {
    private val schedulers: AppRxSchedulers by kodeinInstance.instance()
    private val queryItems: QueryItems by kodeinInstance.instance()
    private val loadingState = RxLoadingCounter()

    init {
        loadingState.observable
            .execute { copy(isLoading = it() ?: false) }

        queryItems.observe()
            .toObservable()
            .subscribeOn(schedulers.io)
            .doOnError(logger::e)
            .execute {
                copy(items = it())
            }

        queryItems.launchQuery(QueryItems.Params.ByCreator("Franz Kafka"))
    }

    private fun QueryItems.launchQuery(params: QueryItems.Params) {
        setParams(params)
        scope.launchInteractor(this, QueryItems.ExecuteParams())
    }

    companion object : MvRxViewModelFactory<CollectionViewModel, CollectionViewState> {
        // This *must* be @JvmStatic for performance reasons.
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: CollectionViewState
        ): CollectionViewModel {
            return CollectionViewModel()
        }
    }
}
