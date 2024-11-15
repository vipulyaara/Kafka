package com.kafka.library.bookshelf

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.entities.ListItem
import com.kafka.domain.interactors.library.AddToBookshelf
import com.kafka.domain.observers.library.ObserveBookshelfItems
import com.kafka.domain.observers.library.ObserveBookshelves
import kotlinx.coroutines.flow.combine
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class BookshelfDetailViewModel(
    @Assisted private val savedStateHandle: SavedStateHandle,
    observeBookshelves: ObserveBookshelves,
    observeBookshelfItems: ObserveBookshelfItems,
    private val addToBookshelf: AddToBookshelf,
    private val analytics: Analytics
) : ViewModel() {
    private val bookshelfId = savedStateHandle.get<String>("bookshelfId")!!

    val state = combine(observeBookshelves.flow, observeBookshelfItems.flow) { shelves, items ->
        BookshelfDetailState(items = items)
    }.stateInDefault(viewModelScope, BookshelfDetailState())

    init {
        observeBookshelves(Unit)
    }
}

data class BookshelfDetailState(
    val items: List<ListItem> = listOf(),
)
