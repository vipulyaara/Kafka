package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.homepage.HomepageRepository
import com.kafka.data.feature.item.ItemRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateHomepage @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val homepageRepository: HomepageRepository,
    private val itemRepository: ItemRepository,
    private val itemDao: ItemDao,
    private val supabase: Supabase,
) : Interactor<Unit, Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            // todo: see if we need a refresh mechanism
            val unFetchedIds = homepageRepository.getHomepageIds()
//                .map { ids ->
//                ids.filter { !itemRepository.exists(it) }
//            }

            // If there are less than 50 un-fetched IDs, we make a single request
            val formattedIds = if (unFetchedIds.sumOf { it.size } <= 50) {
                listOf(unFetchedIds.flatten())
            } else {
                unFetchedIds
            }

            formattedIds.forEach { ids ->
                val items = supabase.items.select {
                    filter { Item::itemId isIn ids }
                }.decodeList<Item>()

                itemDao.insertAll(items)
            }
        }
    }
}
