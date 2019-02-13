package com.airtel.data.config.di

import com.airtel.data.feature.book.BookDataSource
import com.airtel.data.feature.book.BookRepository
import com.airtel.data.feature.book.GetBook
import com.airtel.data.feature.book.LocalBookStore
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

/**
 * @author Vipul Kumar; dated 21/10/18.
 */

val bookDetailModule = Kodein.Module("bookDetailModule") {

    bind<BookRepository>() with provider {
        BookRepository(instance(), instance())
    }

    bind<GetBook>() with provider {
        GetBook(instance(), instance())
    }

    bind<LocalBookStore>() with provider {
        LocalBookStore(instance(), instance())
    }

    bind<BookDataSource>() with provider {
        BookDataSource()
    }
}
