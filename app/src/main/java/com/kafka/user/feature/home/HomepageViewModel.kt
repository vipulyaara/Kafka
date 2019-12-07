package com.kafka.user.feature.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.kafka.data.data.config.ProcessLifetime
import com.kafka.data.data.interactor.launchObserve
import com.kafka.data.entities.Content
import com.kafka.data.feature.content.ContentRepository
import com.kafka.data.feature.content.ObserveContent
import com.kafka.data.feature.content.UpdateContent
import com.kafka.data.model.Event
import com.kafka.data.model.RailItem
import com.kafka.data.util.AppCoroutineDispatchers
import com.kafka.user.feature.common.BaseMvRxViewModel
import com.kafka.user.ui.ObservableLoadingCounter
import com.kafka.user.ui.collectFrom
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseMvRxViewModel] to provide data for homepage.
 */
class HomepageViewModel @AssistedInject constructor(
    @Assisted initialState: HomepageViewState,
    private val loadingState: ObservableLoadingCounter,
    appCoroutineDispatchers: AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    contentRepository: ContentRepository
) : BaseMvRxViewModel<HomepageViewState>(HomepageViewState()), HomepageController.Callbacks {


    private val _navigateToContentDetailAction = MutableLiveData<Event<Content>>()
    val navigateToContentDetailAction: LiveData<Event<Content>>
        get() = _navigateToContentDetailAction

    private val observeContents = arrayListOf(ObserveContent(appCoroutineDispatchers, contentRepository))
    private val updateContents = arrayListOf(UpdateContent(appCoroutineDispatchers, processScope, contentRepository))

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

    fun refresh() {
        updateContents.forEach { updateContent ->
            updateContent(UpdateContent.Params.ByCreator("Kafka")).also {
                viewModelScope.launch {
                    loadingState.collectFrom(it)
                }
            }
        }
    }

    private fun HomepageViewState.onItemsFetched(list: List<Content>?): HomepageViewState {
        val new =
            items?.toMutableSet().also { it?.add(RailItem(it.size.toString() + " Books by Kafka", list)) }
        return copy(items = new)
    }

    override fun onContentClicked(view: View, content: Content) {
        _navigateToContentDetailAction.value = Event(content)
    }

    override fun onBannerClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
