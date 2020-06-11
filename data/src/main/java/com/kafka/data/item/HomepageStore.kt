package com.kafka.data.item

import com.data.base.model.getOrThrow
import com.dropbox.android.external.store4.*
import com.kafka.data.data.db.dao.ItemDao
import com.kafka.data.entities.Item
import com.kafka.data.injection.ForStore
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.asLocalQuery
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

typealias ItemStore = Store<ArchiveQuery, List<Item>>

@InstallIn(ApplicationComponent::class)
@Module
class ItemStoreModule {
    @Singleton
    @Provides
    @ExperimentalStdlibApi
    fun provideStore(
        itemDao: ItemDao,
        itemRemoteDataSource: ItemRemoteDataSource,
        @ForStore scope: CoroutineScope
    ): ItemStore {
        return StoreBuilder
            .from(
                fetcher = nonFlowValueFetcher { itemRemoteDataSource.fetchItemsByCreator(it).getOrThrow() },
                sourceOfTruth = SourceOfTruth.from(
                    reader = { itemDao.observeQueryItems(it.asLocalQuery()) },
                    writer = { _, value: List<Item> -> itemDao.insertAll(value) },
                    delete = { it: ArchiveQuery -> },
                    deleteAll = itemDao::deleteAll
                )
            ).scope(scope).build()
    }
}


fun <F, T> StoreResponse<F?>.map(block: (F?) -> T?) {
    val result = block(dataOrNull())
    when (this) {
        is StoreResponse.Loading -> StoreResponse.Loading(origin)
        is StoreResponse.Data -> StoreResponse.Data(result, origin)
        else -> this
    }
}
