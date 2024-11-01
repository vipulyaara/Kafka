package com.kafka.data.feature.item

import com.kafka.data.api.ArchiveService
import com.kafka.data.entities.Item
import kotlinx.coroutines.withContext
import com.kafka.base.CoroutineDispatchers
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ItemDataSource @Inject constructor(
    private val archiveService: ArchiveService,
    private val itemMapper: ItemMapper,
    private val dispatchers: CoroutineDispatchers,
) {

    suspend fun fetchItemsByQuery(query: String): List<Item> {
        return withContext(dispatchers.io) {
            val response = archiveService.search(query)
            itemMapper.map(response)
        }
    }
}
