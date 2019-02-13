package com.airtel.kafkapp.feature.home

import com.airtel.data.data.db.entities.Book
import com.airtel.kafkapp.feature.common.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class BooksViewState(
    var books: List<Book> = arrayListOf(),
    var isLoading: Boolean = false
) : BaseViewState()
