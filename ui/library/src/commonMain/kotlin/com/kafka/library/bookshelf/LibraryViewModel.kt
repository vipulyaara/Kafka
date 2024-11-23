package com.kafka.library.bookshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.extensions.stateInDefault
import com.kafka.domain.observers.library.ObserveBookshelves
import com.kafka.domain.observers.library.ObserveBookshelves.Params.FetchType
import me.tatarka.inject.annotations.Inject

@Inject
class LibraryViewModel(
    observeBookshelves: ObserveBookshelves,
) : ViewModel() {

    val bookshelves = observeBookshelves.flow.stateInDefault(viewModelScope, listOf())

    init {
        observeBookshelves(ObserveBookshelves.Params(FetchType.Library))
    }
}
