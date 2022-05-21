package com.kafka.data.feature.item

import com.kafka.data.api.ArchiveService
import com.kafka.data.entities.Item
import okhttp3.ResponseBody
import org.rekhta.base.AppCoroutineDispatchers
import org.rekhta.base.network.resultApiCall
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ItemRemoteDataSource @Inject constructor(
    private val archiveService: ArchiveService,
    private val itemMapper: ItemMapper,
    private val dispatchers: AppCoroutineDispatchers
) {

    suspend fun fetchItemsByQuery(query: String): Result<List<Item>> {
        return resultApiCall(dispatchers.io) {
            itemMapper.map(archiveService.search(query))
        }
    }

    suspend fun downloadFile(url: String): Result<ResponseBody> {
        return resultApiCall(dispatchers.io) {
            archiveService.downloadFileWithDynamicUrlSync(url)
        }
    }
}
