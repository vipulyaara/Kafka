package com.airtel.data.feature.book

import com.airtel.data.entities.Book
import com.airtel.data.feature.Repository
import com.airtel.data.model.data.Result
import com.airtel.data.model.data.Success

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class BookRepository constructor(
    private val localStore: LocalBookStore,
    private val dataSource: BookDataSource
) : Repository {

    fun observeForFlowable(path: String, searchKeyword: String) = localStore.booksFlowable(path, searchKeyword)

    suspend fun updateBooks(path: String, searchKeyword: String): Result<List<Book>> {
        val result = dataSource.fetchBooks(path, searchKeyword)
        when (result) {
            is Success -> {
                result.data.let {
                    localStore.saveBooks(it)
                }
            }
        }
        return result
    }
}
