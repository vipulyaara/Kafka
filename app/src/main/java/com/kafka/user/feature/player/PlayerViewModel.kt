package com.kafka.user.feature.player

import android.app.Application
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
import com.kafka.user.feature.home.HomepageViewState
import com.kafka.user.player.Player
import com.kafka.user.player.PlayerService
import com.kafka.user.ui.RxLoadingCounter
import io.reactivex.Observable
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for search.
 */
@UseInjection
class PlayerViewModel : BaseViewModel<PlayerViewState>(
    PlayerViewState()
) {
    private val application: Application by kodeinInstance.instance()
    private val loadingState = RxLoadingCounter()

    init {
        loadingState.observable.execute { copy(isLoading = it() ?: false) }
    }

    companion object : MvRxViewModelFactory<PlayerViewModel, PlayerViewState> {
        // This *must* be @JvmStatic for performance reasons.
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: PlayerViewState
        ): PlayerViewModel {
            return PlayerViewModel()
        }
    }
}
