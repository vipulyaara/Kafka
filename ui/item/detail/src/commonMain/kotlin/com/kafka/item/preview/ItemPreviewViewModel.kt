package com.kafka.item.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.entities.Item
import com.kafka.domain.observers.ObserveItem
import com.kafka.navigation.graph.Screen.ItemDetail.Origin
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ItemPreviewViewModel(
    observeItem: ObserveItem,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val itemId = savedStateHandle.get<String>("itemId")!!
    private val origin = Origin.find(savedStateHandle.get<String>("origin"))

    val state = observeItem.flow
        .map { item -> ItemPreviewState(item = item, origin = origin) }
        .stateInDefault(viewModelScope, ItemPreviewState())

    init {
        observeItem(itemId)
    }
}

data class ItemPreviewState(val item: Item? = null, val origin: Origin = Origin.Unknown)
