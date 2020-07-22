package com.kafka.content.data.item

import com.data.base.model.ArchiveQuery
import com.data.base.model.asLocalQuery
import com.data.base.model.getOrThrow
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.nonFlowValueFetcher
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

typealias ItemStore = Store<ArchiveQuery, List<Item>>

@InstallIn(ApplicationComponent::class)
@Module
class ItemStoreModule {
    @Singleton
    @Provides
    fun provideStore(itemDao: ItemDao, itemRemoteDataSource: ItemRemoteDataSource): ItemStore {
        return StoreBuilder
            .from(
                fetcher = nonFlowValueFetcher { itemRemoteDataSource.fetchItemsByCreator(it).getOrThrow() },
                sourceOfTruth = SourceOfTruth.from(
                    reader = { itemDao.observeQueryItems(it.asLocalQuery()) },
                    writer = { _, value: List<Item> -> itemDao.insertAll(value) },
                    delete = { it: ArchiveQuery -> },
                    deleteAll = itemDao::deleteAll
                )
            ).build()
    }
}
