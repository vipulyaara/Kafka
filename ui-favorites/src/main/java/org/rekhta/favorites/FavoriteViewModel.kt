package org.rekhta.favorites

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.kafka.common.UiMessage
import org.kafka.common.UiMessageManager
import org.rekhta.analytics.LogContentEvent
import org.rekhta.domain.interactors.ToggleFavorite
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val toggleFavorite: ToggleFavorite,
    private val logContentEvent: LogContentEvent,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
}

@Immutable
data class FavoriteViewState(val message: UiMessage? = null)
