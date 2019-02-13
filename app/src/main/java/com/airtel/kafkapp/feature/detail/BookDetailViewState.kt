package com.airtel.kafkapp.feature.detail

import com.airtel.data.data.db.entities.Book
import com.airtel.kafkapp.feature.common.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class BookDetailViewState(
    var book: Book = Book(bookId = ""),
    var isLoading: Boolean = false
) : BaseViewState()
