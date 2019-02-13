package com.airtel.kafka

import com.airtel.data.config.kodeinInstance
import com.airtel.data.data.db.entities.Book
import com.airtel.data.model.data.Resource
import com.airtel.kafka.feature.content.BooksViewModel
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 02/01/19.
 */
internal object BookService {
    private val booksViewModel: BooksViewModel by kodeinInstance.instance()

    //TODO
    fun getBookDetail(bookId: String): ServiceRequest<Resource<Book>> {
        return ServiceRequest { booksViewModel.getBookDetail(bookId) }
    }

    fun getBooksByAuthor(authorLastName: String): ServiceRequest<Resource<List<Book>>> {
        return ServiceRequest { booksViewModel.getBookByAuthor(authorLastName) }
    }
}
