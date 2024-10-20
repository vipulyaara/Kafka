package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.Service
import com.kafka.base.appService
import com.kafka.base.domain.Interactor
import com.kafka.base.extensions.mapAsync
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import com.kafka.data.feature.SupabaseDb
import com.kafka.data.feature.homepage.HomepageRepository
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import com.kafka.domain.interactors.query.BuildRemoteQuery
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateHomepage @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val homepageRepository: HomepageRepository,
    private val itemRepository: ItemRepository,
    private val itemDao: ItemDao,
    private val supabaseDb: SupabaseDb,
    private val buildRemoteQuery: BuildRemoteQuery,
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            if (appService == Service.Archive) {
                fetchBooksFromArchive()
            } else {
                val supabaseItems = supabaseDb.books.select().decodeList<Item>()
                itemDao.insertAll(supabaseItems)
            }
        }
    }

    private suspend fun fetchBooksFromArchive(): List<Unit> {
        val unFetchedIds = homepageRepository.getHomepageIds().map { ids ->
            ids.filter { !itemRepository.exists(it) }
        }

        // If there are less than 50 un-fetched IDs, we make a single request
        val formattedIds = if (unFetchedIds.sumOf { it.size } <= 50) {
            listOf(unFetchedIds.flatten())
        } else {
            unFetchedIds
        }

        return formattedIds.mapAsync { ids ->
            if (ids.isNotEmpty()) {
                val query = ArchiveQuery().booksByIdentifiers(ids)
                val items = itemRepository.updateQuery(buildRemoteQuery(query))
                itemRepository.saveItems(items.filterNot { it.isInappropriate })
            }
        }
    }
}
