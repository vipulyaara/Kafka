package com.airtel.kafka.feature.content

import androidx.lifecycle.LiveData
import com.airtel.data.data.annotations.UseInjection
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.entities.Book
import com.airtel.data.feature.book.GetBook
import com.airtel.data.model.data.Resource
import com.airtel.data.util.AppRxSchedulers
import com.airtel.kafka.extensions.map
import com.airtel.kafka.feature.common.BaseViewModel
import com.airtel.kafka.model.ResourceLiveData
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for content detail.
 */
@UseInjection
internal class BooksViewModel : BaseViewModel() {
    private val schedulers: AppRxSchedulers by kodeinInstance.instance()
    private val getBook: GetBook by kodeinInstance.instance()

    private val contentDetailResult = ResourceLiveData<List<Book>>(schedulers)
    private val booksByAuthor = ResourceLiveData<List<Book>>(schedulers)

    init {
        contentDetailResult.loading()?.disposeOnClear()

        contentDetailResult.data(
            getBook.observe().toObservable()
        )?.disposeOnClear()

        booksByAuthor.loading()?.disposeOnClear()

        booksByAuthor.data(
            getBook.observe().toObservable()
        )?.disposeOnClear()
    }

    private fun updateBookDetail(bookId: String) {
        getBook.setParams(GetBook.Params("id", bookId))
        contentDetailResult.launchInteractor(scope, getBook, GetBook.ExecuteParams())
    }

    private fun updateBookByAuthor(authorLastName: String) {
        getBook.setParams(GetBook.Params("author", authorLastName))
        booksByAuthor.launchInteractor(scope, getBook, GetBook.ExecuteParams())
    }

    private fun updateBookByGenres(genre: String) {
        getBook.setParams(GetBook.Params("genre", genre))
        contentDetailResult.launchInteractor(scope, getBook, GetBook.ExecuteParams())
    }

    fun getBookDetail(bookId: String): LiveData<Resource<Book>> {
        updateBookDetail(bookId)
        return contentDetailResult.map { it.map { list -> list?.get(0) } }
    }

    fun getBookByAuthor(authorLastName: String): LiveData<Resource<List<Book>>> {
        updateBookByAuthor(authorLastName)
        return booksByAuthor
    }
}
