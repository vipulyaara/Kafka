package com.kafka.data.feature.item

import com.kafka.data.api.ArchiveService
import com.kafka.data.entities.Item
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.network.resultApiCall
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ItemDataSource @Inject constructor(
    private val archiveService: ArchiveService,
    private val itemMapper: ItemMapper,
    private val dispatchers: CoroutineDispatchers,
) {

    suspend fun fetchItemsByQuery(query: String): Result<List<Item>> {
        return resultApiCall(dispatchers.io) {
            val response = archiveService.search(query)
            itemMapper.map(response)
        }
    }
}
