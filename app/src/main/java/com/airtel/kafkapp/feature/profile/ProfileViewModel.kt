package com.airtel.kafkapp.feature.profile

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airtel.data.data.annotations.UseInjection
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.feature.launchInteractor
import com.airtel.data.feature.query.QueryItems
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
class ProfileViewModel : BaseViewModel<ProfileViewState>(
    ProfileViewState()
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

    companion object : MvRxViewModelFactory<ProfileViewModel, ProfileViewState> {
        // This *must* be @JvmStatic for performance reasons.
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: ProfileViewState
        ): ProfileViewModel {
            return ProfileViewModel()
        }
    }
}
