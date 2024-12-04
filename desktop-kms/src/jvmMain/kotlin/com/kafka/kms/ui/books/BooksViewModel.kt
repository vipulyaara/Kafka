package com.kafka.kms.ui.books

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kafka.data.entities.ItemDetail
import com.kafka.kms.domain.usecase.ObserveAllBooks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

@Inject
class BooksViewModel(private val observeAllBooks: ObserveAllBooks) : ViewModel() {
    var state by mutableStateOf(BooksState())
        private set

    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    init {
        loadBooks()
    }

    private fun loadBooks() {
        observeAllBooks.getBooks()
            .onEach { books ->
                state = state.copy(
                    books = books,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }
}

data class BooksState(
    val books: List<ItemDetail> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
