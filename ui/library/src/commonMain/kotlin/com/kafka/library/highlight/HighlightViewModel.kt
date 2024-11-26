package com.kafka.library.highlight

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.extensions.stateInDefault
import com.kafka.domain.observers.library.ObserveHighlights
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class HighlightViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeHighlights: ObserveHighlights,
) : ViewModel() {
    val itemId = savedStateHandle.get<String>("itemId")

    val highlights = observeHighlights.flow.stateInDefault(viewModelScope, emptyList())

    init {
        observeHighlights(ObserveHighlights.Params(itemId))
    }
}
