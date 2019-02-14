package com.airtel.data.feature.book

import com.airtel.data.entities.Book
import com.airtel.data.entities.toBook
import com.airtel.data.data.mapper.Mapper
import com.airtel.data.extensions.executeWithRetry
import com.airtel.data.feature.common.DataSource
import com.airtel.data.model.data.Result
import com.airtel.data.model.book.BookListResponse
import com.airtel.data.util.NetworkConstants.bookDetailUrl

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class BookDataSource : DataSource() {
    private val mapper = object : Mapper<BookListResponse, List<Book>> {
        override fun map(from: BookListResponse): List<Book> {
            return from.books?.map { it.toBook() } ?: arrayListOf()
        }
    }

    suspend fun fetchBooks(path: String, searchKeyword: String): Result<List<Book>> {
        return retrofitRunner.executeForResponse(mapper) {
            archiveService
                .getBooks(
                    bookDetailUrl(path, searchKeyword),
                    "json")
                .executeWithRetry()
        }
    }
}
