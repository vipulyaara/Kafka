package com.kafka.library.bookshelf.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.entities.Bookshelf
import com.kafka.data.entities.ItemDetail
import com.kafka.domain.interactors.library.AddToBookshelf
import com.kafka.domain.observers.ObserveItemDetail
import com.kafka.domain.observers.library.ObserveBookshelves
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class AddToBookshelfViewModel(
    @Assisted private val savedStateHandle: SavedStateHandle,
    observeBookshelves: ObserveBookshelves,
    observeItemDetail: ObserveItemDetail,
    private val addToBookshelf: AddToBookshelf,
    private val analytics: Analytics,
) : ViewModel() {
    private val itemId = savedStateHandle.get<String>("itemId")!!

    val state = combine(observeBookshelves.flow, observeItemDetail.flow) { shelves, item ->
        AddToBookshelfState(bookshelves = shelves, itemDetail = item)
    }.stateInDefault(viewModelScope, AddToBookshelfState())

    init {
        observeBookshelves(Unit)
        observeItemDetail(ObserveItemDetail.Param(itemId))
    }

    fun addToBookShelf(bookshelf: Bookshelf) {
        analytics.log { addToBookshelf(itemId, bookshelf.id) }
        viewModelScope.launch {
            addToBookshelf(AddToBookshelf.Params(itemId, bookshelf, true)) // todo: find correct boolean to pass
        }
    }
}

data class AddToBookshelfState(
    val bookshelves: List<Bookshelf> = listOf(),
    val itemDetail: ItemDetail? = null
)
