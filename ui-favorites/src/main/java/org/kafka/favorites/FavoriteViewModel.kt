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
import org.kafka.domain.interactors.ToggleFavorite
import org.kafka.domain.observers.ObserveFollowedItems
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val toggleFavorite: ToggleFavorite,
    private val observeFollowedItems: ObserveFollowedItems
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state: StateFlow<FavoriteViewState> = combine(
        observeFollowedItems.flow,
        loadingCounter.observable,
        uiMessageManager.message,
    ) { items, isLoading, message ->
        FavoriteViewState(
            items = items,
            message = message,
            isLoading = isLoading
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = FavoriteViewState(),
    )

    init {
        observeFollowedItems(Unit)
    }
}

@Immutable
data class FavoriteViewState(
    val items: List<Item>? = null,
    val isLoading: Boolean = false,
    val message: UiMessage? = null
)
