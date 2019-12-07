package com.kafka.user.feature.home

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.kafka.data.data.interactor.launchObserve
import com.kafka.data.entities.Content
import com.kafka.data.feature.content.ContentRepository
import com.kafka.data.feature.content.ObserveContent
import com.kafka.data.model.RailItem
import com.kafka.data.util.AppCoroutineDispatchers
import com.kafka.user.feature.common.BaseMvRxViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import com.kafka.user.ui.ObservableLoadingCounter

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseMvRxViewModel] to provide data for homepage.
 */
class HomepageViewModel @AssistedInject constructor(
    @Assisted initialState: HomepageViewState,
    private val loadingState: ObservableLoadingCounter,
    appCoroutineDispatchers: AppCoroutineDispatchers,
    contentRepository: ContentRepository
) : BaseMvRxViewModel<HomepageViewState>(HomepageViewState()) {

    private val observeContents = arrayListOf(ObserveContent(appCoroutineDispatchers, contentRepository))

    init {
        viewModelScope.launch {
            loadingState.observable
                .distinctUntilChanged()
                .debounce(1200)
                .collect {
                    setState { copy(isLoading = it) }
                }
        }

        observeContents.forEach { observeContent ->
            viewModelScope.launchObserve(observeContent) { flow ->
                flow.execute {
                    onItemsFetched(it())
                }
            }

            observeContent(ObserveContent.Params.ByCreator("Kafka"))
        }
    }

    private fun HomepageViewState.onItemsFetched(list: List<Content>?): HomepageViewState {
        val new =
            items?.toMutableSet().also { it?.add(RailItem(it.size.toString() + " Books by Kafka", list)) }
        return copy(items = new)
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
