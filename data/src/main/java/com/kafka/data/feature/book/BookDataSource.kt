package com.kafka.data.feature.book

import com.kafka.data.entities.Book
import com.kafka.data.entities.toBook
import com.kafka.data.data.mapper.Mapper
import com.kafka.data.extensions.executeWithRetry
import com.kafka.data.feature.common.DataSource
import com.kafka.data.model.data.Result
import com.kafka.data.model.book.BookListResponse
import com.kafka.data.util.NetworkConstants.bookDetailUrl

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
