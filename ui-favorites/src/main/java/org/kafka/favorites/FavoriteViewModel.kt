package org.kafka.favorites

import androidx.compose.runtime.Immutable
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.Item
import com.kafka.data.prefs.PreferencesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessage
import org.kafka.common.UiMessageManager
import org.kafka.domain.observers.ObserveDownloadedItems
import org.kafka.domain.observers.ObserveFollowedItems
import org.kafka.ui.components.item.LayoutType
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    observeDownloadedItems: ObserveDownloadedItems,
    observeFollowedItems: ObserveFollowedItems,
    preferencesStore: PreferencesStore
) : ViewModel() {
    private val preferenceKey get() = stringPreferencesKey("layout")
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    private val layoutType = preferencesStore.getStateFlow(
        keyName = preferenceKey, scope = viewModelScope, initialValue = LayoutType.List.name
    )

    val state: StateFlow<FavoriteViewState> = combine(
        observeFollowedItems.flow,
        observeDownloadedItems.flow,
        layoutType.map { LayoutType.valueOf(it) },
        loadingCounter.observable,
        uiMessageManager.message,
    ) { favorites, downloaded, layout, isLoading, message ->
        FavoriteViewState(
            favoriteItems = favorites,
            downloadedItems = downloaded,
            message = message,
            isLoading = isLoading,
            layoutType = layout
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = FavoriteViewState(),
    )

    fun updateLayoutType(layoutType: LayoutType) {
        this.layoutType.value = layoutType.name
    }

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
    val message: UiMessage? = null,
    val layoutType: LayoutType = LayoutType.List
)
