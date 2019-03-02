package com.airtel.data.data.config.di

import com.airtel.data.data.db.MiddlewareDb
import com.airtel.data.data.db.dao.BookDao
import com.airtel.data.data.db.dao.ItemDetailDao
import com.airtel.data.data.db.dao.QueryDao
import com.airtel.data.data.db.dao.SearchDao
import com.airtel.data.feature.book.BookDataSource
import com.airtel.data.feature.book.BookRepository
import com.airtel.data.feature.book.GetBook
import com.airtel.data.feature.book.LocalBookStore
import com.airtel.data.feature.detail.GetItemDetail
import com.airtel.data.feature.detail.ItemDetailDataSource
import com.airtel.data.feature.detail.ItemDetailRepository
import com.airtel.data.feature.detail.LocalItemDetailStore
import com.airtel.data.feature.query.QueryItems
import com.airtel.data.feature.query.QueryLocalSource
import com.airtel.data.feature.query.QueryRemoteSource
import com.airtel.data.feature.query.QueryRepository
import com.airtel.data.feature.search.SearchItems
import com.airtel.data.feature.search.SearchLocalSource
import com.airtel.data.feature.search.SearchRemoteSource
import com.airtel.data.feature.search.SearchRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

/**
 * @author Vipul Kumar; dated 21/10/18.
 */

val dataModule = Kodein.Module("dataModule") {

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

    bind<BookDao>() with singleton {
        instance<MiddlewareDb>().contentDao()
    }

    bind<ItemDetailRepository>() with provider {
        ItemDetailRepository(instance(), instance())
    }

    bind<GetItemDetail>() with provider {
        GetItemDetail(instance(), instance())
    }

    bind<LocalItemDetailStore>() with provider {
        LocalItemDetailStore(instance(), instance())
    }

    bind<ItemDetailDataSource>() with provider {
        ItemDetailDataSource()
    }

    bind<ItemDetailDao>() with singleton {
        instance<MiddlewareDb>().itemDetailDao()
    }

    bind<SearchRepository>() with provider {
        SearchRepository(instance(), instance())
    }

    bind<SearchItems>() with provider {
        SearchItems(instance(), instance())
    }

    bind<QueryRepository>() with provider {
        QueryRepository(instance(), instance())
    }

    bind<QueryRemoteSource>() with provider {
        QueryRemoteSource()
    }

    bind<QueryDao>() with provider {
        instance<MiddlewareDb>().queryDao()
    }

    bind<QueryLocalSource>() with provider {
        QueryLocalSource(instance(), instance())
    }

    bind<QueryItems>() with provider {
        QueryItems(instance())
    }

    bind<SearchLocalSource>() with provider {
        SearchLocalSource(instance(), instance())
    }

    bind<SearchRemoteSource>() with provider {
        SearchRemoteSource()
    }

    bind<SearchDao>() with singleton {
        instance<MiddlewareDb>().searchDao()
    }
}
