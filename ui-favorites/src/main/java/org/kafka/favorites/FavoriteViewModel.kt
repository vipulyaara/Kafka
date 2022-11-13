package org.kafka.favorites

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessage
import org.kafka.common.UiMessageManager
import org.kafka.domain.observers.ObserveDownloadedItems
import org.kafka.domain.observers.ObserveFollowedItems
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    observeDownloadedItems: ObserveDownloadedItems,
    observeFollowedItems: ObserveFollowedItems
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state: StateFlow<FavoriteViewState> = combine(
        observeFollowedItems.flow,
        observeDownloadedItems.flow,
        loadingCounter.observable,
        uiMessageManager.message,
    ) { favorites, downloaded, isLoading, message ->
        FavoriteViewState(
            favoriteItems = favorites,
            downloadedItems = downloaded,
            message = message,
            isLoading = isLoading
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = FavoriteViewState(),
    )

    init {
        observeFollowedItems(Unit)
        observeDownloadedItems(Unit)
    }
}

@Immutable
data class FavoriteViewState(
    val favoriteItems: List<Item> = emptyList(),
    val downloadedItems: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val message: UiMessage? = null
)
